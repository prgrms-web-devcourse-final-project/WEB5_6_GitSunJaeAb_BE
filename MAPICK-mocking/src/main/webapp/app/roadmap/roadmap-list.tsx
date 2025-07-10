import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError } from 'app/common/utils';
import { MapDTO } from 'app/roadmap/roadmap-model';
import axios from 'axios';
import useDocumentTitle from 'app/common/use-document-title';


export default function RoadmapList() {
  const { t } = useTranslation();
  useDocumentTitle(t('map.list.headline'));

  const [maps, setMaps] = useState<MapDTO[]>([]);
  const navigate = useNavigate();

  const getAllMaps = async () => {
    try {
      const response = await axios.get('/api/maps');
      setMaps(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/maps/' + id);
      navigate('/maps', {
            state: {
              msgInfo: t('map.delete.success')
            }
          });
      getAllMaps();
    } catch (error: any) {
      if (error?.response?.data?.code === 'REFERENCED') {
        const messageParts = error.response.data.message.split(',');
        navigate('/maps', {
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
    getAllMaps();
  }, []);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('map.list.headline')}</h1>
      <div>
        <Link to="/maps/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('map.list.createNew')}</Link>
      </div>
    </div>
    {!maps || maps.length === 0 ? (
    <div>{t('map.list.empty')}</div>
    ) : (
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('map.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('map.title.label')}</th>
            <th scope="col" className="text-left p-2">{t('map.thumbnail.label')}</th>
            <th scope="col" className="text-left p-2">{t('map.isPublic.label')}</th>
            <th scope="col" className="text-left p-2">{t('map.isAnimated.label')}</th>
            <th scope="col" className="text-left p-2">{t('map.likeCount.label')}</th>
            <th scope="col" className="text-left p-2">{t('map.viewCount.label')}</th>
            <th scope="col" className="text-left p-2">{t('map.mapType.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {maps.map((map) => (
          <tr key={map.id} className="odd:bg-gray-100">
            <td className="p-2">{map.id}</td>
            <td className="p-2">{map.title}</td>
            <td className="p-2">{map.thumbnail}</td>
            <td className="p-2">{map.isPublic?.toString()}</td>
            <td className="p-2">{map.isAnimated?.toString()}</td>
            <td className="p-2">{map.likeCount}</td>
            <td className="p-2">{map.viewCount}</td>
            <td className="p-2">{map.mapType}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/maps/edit/' + map.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('map.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(map.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('map.list.delete')}</button>
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
