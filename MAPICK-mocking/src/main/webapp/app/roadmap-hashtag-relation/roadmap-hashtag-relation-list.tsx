import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError } from 'app/common/utils';
import { RoadmapHashtagRelationDTO } from 'app/roadmap-hashtag-relation/roadmap-hashtag-relation-model';
import axios from 'axios';
import useDocumentTitle from 'app/common/use-document-title';


export default function RoadmapHashtagRelationList() {
  const { t } = useTranslation();
  useDocumentTitle(t('roadmapHashtagRelation.list.headline'));

  const [roadmapHashtagRelations, roadmapValuesResponse] = useState<RoadmapHashtagRelationDTO[]>([]);
  const navigate = useNavigate();

  const getAllRoadmapHashtagRelations = async () => {
    try {
      const response = await axios.get('/roadmapHashtagRelations');
      roadmapValuesResponse(response.data.content);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/roadmapHashtagRelations/' + id);
      navigate('/roadmapHashtagRelations', {
            state: {
              msgInfo: t('roadmapHashtagRelation.delete.success')
            }
          });
      getAllRoadmapHashtagRelations();
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    getAllRoadmapHashtagRelations();
  }, []);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('roadmapHashtagRelation.list.headline')}</h1>
      <div>
        <Link to="/roadmapHashtagRelations/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('roadmapHashtagRelation.list.createNew')}</Link>
      </div>
    </div>
    {!roadmapHashtagRelations || roadmapHashtagRelations.length === 0 ? (
    <div>{t('roadmapHashtagRelation.list.empty')}</div>
    ) : (
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('roadmapHashtagRelation.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('roadmapHashtagRelation.createdAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('roadmapHashtagRelation.hashtag.label')}</th>
            <th scope="col" className="text-left p-2">{t('roadmapHashtagRelation.roadmap.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {roadmapHashtagRelations.map((roadmapHashtagRelation) => (
          <tr key={roadmapHashtagRelation.id} className="odd:bg-gray-100">
            <td className="p-2">{roadmapHashtagRelation.id}</td>
            <td className="p-2">{roadmapHashtagRelation.createdAt}</td>
            <td className="p-2">{roadmapHashtagRelation.hashtag}</td>
            <td className="p-2">{roadmapHashtagRelation.roadmap}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/roadmapHashtagRelations/edit/' + roadmapHashtagRelation.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('roadmapHashtagRelation.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(roadmapHashtagRelation.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('roadmapHashtagRelation.list.delete')}</button>
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
