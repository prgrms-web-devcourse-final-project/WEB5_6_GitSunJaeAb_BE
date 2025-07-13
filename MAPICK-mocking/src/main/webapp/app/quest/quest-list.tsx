import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError } from 'app/common/utils';
import { QuestDTO } from 'app/quest/quest-model';
import axios from 'axios';
import useDocumentTitle from 'app/common/use-document-title';


export default function QuestList() {
  const { t } = useTranslation();
  useDocumentTitle(t('quest.list.headline'));

  const [quests, setQuests] = useState<QuestDTO[]>([]);
  const navigate = useNavigate();

  const getAllQuests = async () => {
    try {
      const response = await axios.get('/quests');
      setQuests(response.data.content);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/quests/' + id);
      navigate('/quests', {
            state: {
              msgInfo: t('quest.delete.success')
            }
          });
      getAllQuests();
    } catch (error: any) {
      if (error?.response?.data?.code === 'REFERENCED') {
        const messageParts = error.response.data.message.split(',');
        navigate('/quests', {
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
    getAllQuests();
  }, []);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('quest.list.headline')}</h1>
      <div>
        <Link to="/quests/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('quest.list.createNew')}</Link>
      </div>
    </div>
    {!quests || quests.length === 0 ? (
    <div>{t('quest.list.empty')}</div>
    ) : (
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('quest.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('quest.title.label')}</th>
            <th scope="col" className="text-left p-2">{t('quest.questImage.label')}</th>
            <th scope="col" className="text-left p-2">{t('quest.isActive.label')}</th>
            <th scope="col" className="text-left p-2">{t('quest.createdAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('quest.completedAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('quest.updatedAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('quest.deletedAt.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {quests.map((quest) => (
          <tr key={quest.id} className="odd:bg-gray-100">
            <td className="p-2">{quest.id}</td>
            <td className="p-2">{quest.title}</td>
            <td className="p-2">{quest.questImage}</td>
            <td className="p-2">{quest.isActive?.toString()}</td>
            <td className="p-2">{quest.createdAt}</td>
            <td className="p-2">{quest.completedAt}</td>
            <td className="p-2">{quest.updatedAt}</td>
            <td className="p-2">{quest.deletedAt}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/quests/edit/' + quest.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('quest.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(quest.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('quest.list.delete')}</button>
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
