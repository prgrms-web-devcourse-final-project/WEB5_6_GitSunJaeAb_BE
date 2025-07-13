import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError } from 'app/common/utils';
import { LayerLibraryDTO } from 'app/layer-library/layer-library-model';
import axios from 'axios';
import useDocumentTitle from 'app/common/use-document-title';


export default function LayerLibraryList() {
  const { t } = useTranslation();
  useDocumentTitle(t('layerLibrary.list.headline'));

  const [layerLibraries, setLayerLibraries] = useState<LayerLibraryDTO[]>([]);
  const navigate = useNavigate();

  const getAllLayerLibraries = async () => {
    try {
      const response = await axios.get('/layerLibraries');
      setLayerLibraries(response.data.content);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/layerLibraries/' + id);
      navigate('/layerLibraries', {
            state: {
              msgInfo: t('layerLibrary.delete.success')
            }
          });
      getAllLayerLibraries();
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    getAllLayerLibraries();
  }, []);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('layerLibrary.list.headline')}</h1>
      <div>
        <Link to="/layerLibraries/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('layerLibrary.list.createNew')}</Link>
      </div>
    </div>
    {!layerLibraries || layerLibraries.length === 0 ? (
    <div>{t('layerLibrary.list.empty')}</div>
    ) : (
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('layerLibrary.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('layerLibrary.createdAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('layerLibrary.member.label')}</th>
            <th scope="col" className="text-left p-2">{t('layerLibrary.layer.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {layerLibraries.map((layerLibrary) => (
          <tr key={layerLibrary.id} className="odd:bg-gray-100">
            <td className="p-2">{layerLibrary.id}</td>
            <td className="p-2">{layerLibrary.createdAt}</td>
            <td className="p-2">{layerLibrary.member}</td>
            <td className="p-2">{layerLibrary.layer}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/layerLibraries/edit/' + layerLibrary.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('layerLibrary.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(layerLibrary.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('layerLibrary.list.delete')}</button>
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
