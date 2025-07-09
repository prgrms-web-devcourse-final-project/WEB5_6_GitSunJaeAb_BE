import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError } from 'app/common/utils';
import { MapHashtagRelationDTO } from 'app/map-hashtag-relation/map-hashtag-relation-model';
import axios from 'axios';
import useDocumentTitle from 'app/common/use-document-title';


export default function MapHashtagRelationList() {
  const { t } = useTranslation();
  useDocumentTitle(t('mapHashtagRelation.list.headline'));

  const [mapHashtagRelations, setMapHashtagRelations] = useState<MapHashtagRelationDTO[]>([]);
  const navigate = useNavigate();

  const getAllMapHashtagRelations = async () => {
    try {
      const response = await axios.get('/api/mapHashtagRelations');
      setMapHashtagRelations(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/mapHashtagRelations/' + id);
      navigate('/mapHashtagRelations', {
            state: {
              msgInfo: t('mapHashtagRelation.delete.success')
            }
          });
      getAllMapHashtagRelations();
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    getAllMapHashtagRelations();
  }, []);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('mapHashtagRelation.list.headline')}</h1>
      <div>
        <Link to="/mapHashtagRelations/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('mapHashtagRelation.list.createNew')}</Link>
      </div>
    </div>
    {!mapHashtagRelations || mapHashtagRelations.length === 0 ? (
    <div>{t('mapHashtagRelation.list.empty')}</div>
    ) : (
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('mapHashtagRelation.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('mapHashtagRelation.createdAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('mapHashtagRelation.hashtag.label')}</th>
            <th scope="col" className="text-left p-2">{t('mapHashtagRelation.map.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {mapHashtagRelations.map((mapHashtagRelation) => (
          <tr key={mapHashtagRelation.id} className="odd:bg-gray-100">
            <td className="p-2">{mapHashtagRelation.id}</td>
            <td className="p-2">{mapHashtagRelation.createdAt}</td>
            <td className="p-2">{mapHashtagRelation.hashtag}</td>
            <td className="p-2">{mapHashtagRelation.map}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/mapHashtagRelations/edit/' + mapHashtagRelation.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('mapHashtagRelation.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(mapHashtagRelation.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('mapHashtagRelation.list.delete')}</button>
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
