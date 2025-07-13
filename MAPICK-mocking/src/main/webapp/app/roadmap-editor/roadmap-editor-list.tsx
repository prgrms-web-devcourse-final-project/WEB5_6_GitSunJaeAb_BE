import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError } from 'app/common/utils';
import { RoadmapEditorDTO } from 'app/roadmap-editor/roadmap-editor-model';
import axios from 'axios';
import useDocumentTitle from 'app/common/use-document-title';


export default function RoadmapEditorList() {
  const { t } = useTranslation();
  useDocumentTitle(t('roadmapEditor.list.headline'));

  const [roadmapEditors, setRoadmapEditors] = useState<RoadmapEditorDTO[]>([]);
  const navigate = useNavigate();

  const getAllRoadmapEditors = async () => {
    try {
      const response = await axios.get('/roadmapEditors');
      setRoadmapEditors(response.data.content);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/roadmapEditors/' + id);
      navigate('/roadmapEditors', {
            state: {
              msgInfo: t('roadmapEditor.delete.success')
            }
          });
      getAllRoadmapEditors();
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    getAllRoadmapEditors();
  }, []);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('roadmapEditor.list.headline')}</h1>
      <div>
        <Link to="/roadmapEditors/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('roadmapEditor.list.createNew')}</Link>
      </div>
    </div>
    {!roadmapEditors || roadmapEditors.length === 0 ? (
    <div>{t('roadmapEditor.list.empty')}</div>
    ) : (
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('roadmapEditor.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('roadmapEditor.permission.label')}</th>
            <th scope="col" className="text-left p-2">{t('roadmapEditor.createdAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('roadmapEditor.updatedAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('roadmapEditor.deletedAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('roadmapEditor.roadmap.label')}</th>
            <th scope="col" className="text-left p-2">{t('roadmapEditor.member.label')}</th>
            <th scope="col" className="text-left p-2">{t('roadmapEditor.invitedBy.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {roadmapEditors.map((roadmapEditor) => (
          <tr key={roadmapEditor.id} className="odd:bg-gray-100">
            <td className="p-2">{roadmapEditor.id}</td>
            <td className="p-2">{roadmapEditor.permission}</td>
            <td className="p-2">{roadmapEditor.createdAt}</td>
            <td className="p-2">{roadmapEditor.updatedAt}</td>
            <td className="p-2">{roadmapEditor.deletedAt}</td>
            <td className="p-2">{roadmapEditor.roadmap}</td>
            <td className="p-2">{roadmapEditor.member}</td>
            <td className="p-2">{roadmapEditor.invitedBy}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/roadmapEditors/edit/' + roadmapEditor.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('roadmapEditor.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(roadmapEditor.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('roadmapEditor.list.delete')}</button>
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
