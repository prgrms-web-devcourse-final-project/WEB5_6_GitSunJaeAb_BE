import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError } from 'app/common/utils';
import { LayerDTO } from 'app/layer/layer-model';
import axios from 'axios';
import useDocumentTitle from 'app/common/use-document-title';


export default function LayerList() {
  const { t } = useTranslation();
  useDocumentTitle(t('layer.list.headline'));

  const [layers, setLayers] = useState<LayerDTO[]>([]);
  const navigate = useNavigate();

  const getAllLayers = async () => {
    try {
      const response = await axios.get('/layers');
      setLayers(response.data.content);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/layers/' + id);
      navigate('/layers', {
            state: {
              msgInfo: t('layer.delete.success')
            }
          });
      getAllLayers();
    } catch (error: any) {
      if (error?.response?.data?.code === 'REFERENCED') {
        const messageParts = error.response.data.message.split(',');
        navigate('/layers', {
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
    getAllLayers();
  }, []);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('layer.list.headline')}</h1>
      <div>
        <Link to="/layers/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('layer.list.createNew')}</Link>
      </div>
    </div>
    {!layers || layers.length === 0 ? (
    <div>{t('layer.list.empty')}</div>
    ) : (
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('layer.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('layer.name.label')}</th>
            <th scope="col" className="text-left p-2">{t('layer.layerSeq.label')}</th>
            <th scope="col" className="text-left p-2">{t('layer.layerTime.label')}</th>
            <th scope="col" className="text-left p-2">{t('layer.createdAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('layer.updatedAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('layer.deletedAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('layer.member.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {layers.map((layer) => (
          <tr key={layer.id} className="odd:bg-gray-100">
            <td className="p-2">{layer.id}</td>
            <td className="p-2">{layer.name}</td>
            <td className="p-2">{layer.layerSeq}</td>
            <td className="p-2">{layer.layerTime}</td>
            <td className="p-2">{layer.createdAt}</td>
            <td className="p-2">{layer.updatedAt}</td>
            <td className="p-2">{layer.deletedAt}</td>
            <td className="p-2">{layer.member}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/layers/edit/' + layer.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('layer.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(layer.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('layer.list.delete')}</button>
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
