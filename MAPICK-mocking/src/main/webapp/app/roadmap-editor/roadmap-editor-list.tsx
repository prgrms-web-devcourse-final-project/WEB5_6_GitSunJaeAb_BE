import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError } from 'app/common/utils';
import { MapEditorDTO } from 'app/roadmap-editor/roadmap-editor-model';
import axios from 'axios';
import useDocumentTitle from 'app/common/use-document-title';


export default function RoadmapEditorList() {
  const { t } = useTranslation();
  useDocumentTitle(t('mapEditor.list.headline'));

  const [roadmapEditors, setMapEditors] = useState<MapEditorDTO[]>([]);
  const navigate = useNavigate();

  const getAllMapEditors = async () => {
    try {
      const response = await axios.get('/api/roadmapEditors');
      setMapEditors(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/roadmapEditors/' + id);
      navigate('/roadmapEditors', {
            state: {
              msgInfo: t('mapEditor.delete.success')
            }
          });
      getAllMapEditors();
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    getAllMapEditors();
  }, []);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('mapEditor.list.headline')}</h1>
      <div>
        <Link to="/roadmapEditors/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('mapEditor.list.createNew')}</Link>
      </div>
    </div>
    {!roadmapEditors || roadmapEditors.length === 0 ? (
    <div>{t('mapEditor.list.empty')}</div>
    ) : (
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('mapEditor.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('mapEditor.permission.label')}</th>
            <th scope="col" className="text-left p-2">{t('mapEditor.createdAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('mapEditor.updatedAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('mapEditor.deletedAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('mapEditor.map.label')}</th>
            <th scope="col" className="text-left p-2">{t('mapEditor.member.label')}</th>
            <th scope="col" className="text-left p-2">{t('mapEditor.invitedBy.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {roadmapEditors.map((mapEditor) => (
          <tr key={mapEditor.id} className="odd:bg-gray-100">
            <td className="p-2">{mapEditor.id}</td>
            <td className="p-2">{mapEditor.permission}</td>
            <td className="p-2">{mapEditor.createdAt}</td>
            <td className="p-2">{mapEditor.updatedAt}</td>
            <td className="p-2">{mapEditor.deletedAt}</td>
            <td className="p-2">{mapEditor.map}</td>
            <td className="p-2">{mapEditor.member}</td>
            <td className="p-2">{mapEditor.invitedBy}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/roadmapEditors/edit/' + mapEditor.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('mapEditor.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(mapEditor.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('mapEditor.list.delete')}</button>
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
