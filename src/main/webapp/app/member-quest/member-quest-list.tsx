import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError } from 'app/common/utils';
import { MemberQuestDTO } from 'app/member-quest/member-quest-model';
import axios from 'axios';
import useDocumentTitle from 'app/common/use-document-title';


export default function MemberQuestList() {
  const { t } = useTranslation();
  useDocumentTitle(t('memberQuest.list.headline'));

  const [memberQuests, setMemberQuests] = useState<MemberQuestDTO[]>([]);
  const navigate = useNavigate();

  const getAllMemberQuests = async () => {
    try {
      const response = await axios.get('/api/memberQuests');
      setMemberQuests(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/memberQuests/' + id);
      navigate('/memberQuests', {
            state: {
              msgInfo: t('memberQuest.delete.success')
            }
          });
      getAllMemberQuests();
    } catch (error: any) {
      if (error?.response?.data?.code === 'REFERENCED') {
        const messageParts = error.response.data.message.split(',');
        navigate('/memberQuests', {
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
    getAllMemberQuests();
  }, []);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('memberQuest.list.headline')}</h1>
      <div>
        <Link to="/memberQuests/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('memberQuest.list.createNew')}</Link>
      </div>
    </div>
    {!memberQuests || memberQuests.length === 0 ? (
    <div>{t('memberQuest.list.empty')}</div>
    ) : (
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('memberQuest.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('memberQuest.status.label')}</th>
            <th scope="col" className="text-left p-2">{t('memberQuest.answer.label')}</th>
            <th scope="col" className="text-left p-2">{t('memberQuest.isRecognized.label')}</th>
            <th scope="col" className="text-left p-2">{t('memberQuest.createdAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('memberQuest.completedAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('memberQuest.updatedAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('memberQuest.deletedAt.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {memberQuests.map((memberQuest) => (
          <tr key={memberQuest.id} className="odd:bg-gray-100">
            <td className="p-2">{memberQuest.id}</td>
            <td className="p-2">{memberQuest.status}</td>
            <td className="p-2">{memberQuest.answer}</td>
            <td className="p-2">{memberQuest.isRecognized}</td>
            <td className="p-2">{memberQuest.createdAt}</td>
            <td className="p-2">{memberQuest.completedAt}</td>
            <td className="p-2">{memberQuest.updatedAt}</td>
            <td className="p-2">{memberQuest.deletedAt}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/memberQuests/edit/' + memberQuest.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('memberQuest.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(memberQuest.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('memberQuest.list.delete')}</button>
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
