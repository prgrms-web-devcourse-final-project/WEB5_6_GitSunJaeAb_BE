import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError } from 'app/common/utils';
import { RoadmapDTO } from 'app/roadmap/roadmap-model';
import axios from 'axios';
import useDocumentTitle from 'app/common/use-document-title';


export default function RoadmapList() {
  const { t } = useTranslation();
  useDocumentTitle(t('roadmap.list.headline'));

  const [roadmaps, setRoadmaps] = useState<RoadmapDTO[]>([]);
  const navigate = useNavigate();

  const getAllRoadmaps = async () => {
    try {
      const response = await axios.get('/roadmap');
      setRoadmaps(response.data.content);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/roadmaps/' + id);
      navigate('/roadmaps', {
            state: {
              msgInfo: t('roadmap.delete.success')
            }
          });
      getAllRoadmaps();
    } catch (error: any) {
      if (error?.response?.data?.code === 'REFERENCED') {
        const messageParts = error.response.data.message.split(',');
        navigate('/roadmaps', {
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
    getAllRoadmaps();
  }, []);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('roadmap.list.headline')}</h1>
      <div>
        <Link to="/roadmaps/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('roadmap.list.createNew')}</Link>
      </div>
    </div>
    {!roadmaps || roadmaps.length === 0 ? (
    <div>{t('roadmap.list.empty')}</div>
    ) : (
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('roadmap.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('roadmap.title.label')}</th>
            <th scope="col" className="text-left p-2">{t('roadmap.thumbnail.label')}</th>
            <th scope="col" className="text-left p-2">{t('roadmap.isPublic.label')}</th>
            <th scope="col" className="text-left p-2">{t('roadmap.isAnimated.label')}</th>
            <th scope="col" className="text-left p-2">{t('roadmap.likeCount.label')}</th>
            <th scope="col" className="text-left p-2">{t('roadmap.viewCount.label')}</th>
            <th scope="col" className="text-left p-2">{t('roadmap.roadmapType.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {roadmaps.map((roadmap) => (
          <tr key={roadmap.id} className="odd:bg-gray-100">
            <td className="p-2">{roadmap.id}</td>
            <td className="p-2">{roadmap.title}</td>
            <td className="p-2">{roadmap.thumbnail}</td>
            <td className="p-2">{roadmap.isPublic?.toString()}</td>
            <td className="p-2">{roadmap.isAnimated?.toString()}</td>
            <td className="p-2">{roadmap.likeCount}</td>
            <td className="p-2">{roadmap.viewCount}</td>
            <td className="p-2">{roadmap.roadmapType}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/roadmaps/edit/' + roadmap.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('roadmap.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(roadmap.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('roadmap.list.delete')}</button>
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
