import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError } from 'app/common/utils';
import { MemberQuestEvidenceDTO } from 'app/member-quest-evidence/member-quest-evidence-model';
import axios from 'axios';
import useDocumentTitle from 'app/common/use-document-title';


export default function MemberQuestEvidenceList() {
  const { t } = useTranslation();
  useDocumentTitle(t('memberQuestEvidence.list.headline'));

  const [memberQuestEvidences, setMemberQuestEvidences] = useState<MemberQuestEvidenceDTO[]>([]);
  const navigate = useNavigate();

  const getAllMemberQuestEvidences = async () => {
    try {
      const response = await axios.get('/memberQuestEvidences');
      setMemberQuestEvidences(response.data.content);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/memberQuestEvidences/' + id);
      navigate('/memberQuestEvidences', {
            state: {
              msgInfo: t('memberQuestEvidence.delete.success')
            }
          });
      getAllMemberQuestEvidences();
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    getAllMemberQuestEvidences();
  }, []);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('memberQuestEvidence.list.headline')}</h1>
      <div>
        <Link to="/memberQuestEvidences/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('memberQuestEvidence.list.createNew')}</Link>
      </div>
    </div>
    {!memberQuestEvidences || memberQuestEvidences.length === 0 ? (
    <div>{t('memberQuestEvidence.list.empty')}</div>
    ) : (
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('memberQuestEvidence.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('memberQuestEvidence.imageUrl.label')}</th>
            <th scope="col" className="text-left p-2">{t('memberQuestEvidence.createdAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('memberQuestEvidence.updatedAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('memberQuestEvidence.deletedAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('memberQuestEvidence.memberQuest.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {memberQuestEvidences.map((memberQuestEvidence) => (
          <tr key={memberQuestEvidence.id} className="odd:bg-gray-100">
            <td className="p-2">{memberQuestEvidence.id}</td>
            <td className="p-2">{memberQuestEvidence.imageUrl}</td>
            <td className="p-2">{memberQuestEvidence.createdAt}</td>
            <td className="p-2">{memberQuestEvidence.updatedAt}</td>
            <td className="p-2">{memberQuestEvidence.deletedAt}</td>
            <td className="p-2">{memberQuestEvidence.memberQuest}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/memberQuestEvidences/edit/' + memberQuestEvidence.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('memberQuestEvidence.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(memberQuestEvidence.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('memberQuestEvidence.list.delete')}</button>
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
