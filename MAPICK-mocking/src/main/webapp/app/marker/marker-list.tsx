import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError } from 'app/common/utils';
import { MarkerDTO } from 'app/marker/marker-model';
import axios from 'axios';
import useDocumentTitle from 'app/common/use-document-title';


export default function MarkerList() {
  const { t } = useTranslation();
  useDocumentTitle(t('marker.list.headline'));

  const [markers, setMarkers] = useState<MarkerDTO[]>([]);
  const navigate = useNavigate();

  const getAllMarkers = async () => {
    try {
      const response = await axios.get('/api/markers');
      setMarkers(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/markers/' + id);
      navigate('/markers', {
            state: {
              msgInfo: t('marker.delete.success')
            }
          });
      getAllMarkers();
    } catch (error: any) {
      if (error?.response?.data?.code === 'REFERENCED') {
        const messageParts = error.response.data.message.split(',');
        navigate('/markers', {
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
    getAllMarkers();
  }, []);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('marker.list.headline')}</h1>
      <div>
        <Link to="/markers/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('marker.list.createNew')}</Link>
      </div>
    </div>
    {!markers || markers.length === 0 ? (
    <div>{t('marker.list.empty')}</div>
    ) : (
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('marker.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('marker.title.label')}</th>
            <th scope="col" className="text-left p-2">{t('marker.lat.label')}</th>
            <th scope="col" className="text-left p-2">{t('marker.lng.label')}</th>
            <th scope="col" className="text-left p-2">{t('marker.color.label')}</th>
            <th scope="col" className="text-left p-2">{t('marker.imageUrl.label')}</th>
            <th scope="col" className="text-left p-2">{t('marker.markerSeq.label')}</th>
            <th scope="col" className="text-left p-2">{t('marker.createdAt.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {markers.map((marker) => (
          <tr key={marker.id} className="odd:bg-gray-100">
            <td className="p-2">{marker.id}</td>
            <td className="p-2">{marker.title}</td>
            <td className="p-2">{marker.lat}</td>
            <td className="p-2">{marker.lng}</td>
            <td className="p-2">{marker.color}</td>
            <td className="p-2">{marker.imageUrl}</td>
            <td className="p-2">{marker.markerSeq}</td>
            <td className="p-2">{marker.createdAt}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/markers/edit/' + marker.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('marker.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(marker.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('marker.list.delete')}</button>
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
