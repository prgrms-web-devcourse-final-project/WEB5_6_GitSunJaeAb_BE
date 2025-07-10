import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError } from 'app/common/utils';
import { RoadMapCategoryRelationDTO } from 'app/roadmap-category-relation/roadmap-category-relation-model';
import axios from 'axios';
import useDocumentTitle from 'app/common/use-document-title';


export default function RoadmapCategoryRelationList() {
  const { t } = useTranslation();
  useDocumentTitle(t('mapCategoryRelation.list.headline'));

  const [roadmapCategoryRelations, setMapCategoryRelations] = useState<RoadMapCategoryRelationDTO[]>([]);
  const navigate = useNavigate();

  const getAllMapCategoryRelations = async () => {
    try {
      const response = await axios.get('/api/roadmapCategoryRelations');
      setMapCategoryRelations(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/roadmapCategoryRelations/' + id);
      navigate('/roadmapCategoryRelations', {
            state: {
              msgInfo: t('mapCategoryRelation.delete.success')
            }
          });
      getAllMapCategoryRelations();
    } catch (error: any) {
      if (error?.response?.data?.code === 'REFERENCED') {
        const messageParts = error.response.data.message.split(',');
        navigate('/roadmapCategoryRelations', {
              state: {
                msgError: t(messageParts[0]!, { id: messageParts[1]! })
              }
            });
        return;
      }
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    getAllMapCategoryRelations();
  }, []);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('mapCategoryRelation.list.headline')}</h1>
      <div>
        <Link to="/roadmapCategoryRelations/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('mapCategoryRelation.list.createNew')}</Link>
      </div>
    </div>
    {!roadmapCategoryRelations || roadmapCategoryRelations.length === 0 ? (
    <div>{t('mapCategoryRelation.list.empty')}</div>
    ) : (
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('mapCategoryRelation.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('mapCategoryRelation.createdAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('mapCategoryRelation.map.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {roadmapCategoryRelations.map((mapCategoryRelation) => (
          <tr key={mapCategoryRelation.id} className="odd:bg-gray-100">
            <td className="p-2">{mapCategoryRelation.id}</td>
            <td className="p-2">{mapCategoryRelation.createdAt}</td>
            <td className="p-2">{mapCategoryRelation.roadmap}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/roadmapCategoryRelations/edit/' + mapCategoryRelation.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('mapCategoryRelation.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(mapCategoryRelation.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('mapCategoryRelation.list.delete')}</button>
              </div>
            </td>
          </tr>
          ))}
        </tbody>
      </table>
    </div>
    )}
  </>);
}
