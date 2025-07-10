import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError } from 'app/common/utils';
import { QuestRankDTO } from 'app/quest-rank/quest-rank-model';
import axios from 'axios';
import useDocumentTitle from 'app/common/use-document-title';


export default function QuestRankList() {
  const { t } = useTranslation();
  useDocumentTitle(t('questRank.list.headline'));

  const [questRanks, setQuestRanks] = useState<QuestRankDTO[]>([]);
  const navigate = useNavigate();

  const getAllQuestRanks = async () => {
    try {
      const response = await axios.get('/questRanks');
      setQuestRanks(response.data.content);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/questRanks/' + id);
      navigate('/questRanks', {
            state: {
              msgInfo: t('questRank.delete.success')
            }
          });
      getAllQuestRanks();
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    getAllQuestRanks();
  }, []);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('questRank.list.headline')}</h1>
      <div>
        <Link to="/questRanks/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('questRank.list.createNew')}</Link>
      </div>
    </div>
    {!questRanks || questRanks.length === 0 ? (
    <div>{t('questRank.list.empty')}</div>
    ) : (
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('questRank.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('questRank.rank.label')}</th>
            <th scope="col" className="text-left p-2">{t('questRank.completedAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('questRank.createdAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('questRank.updatedAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('questRank.deletedAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('questRank.quest.label')}</th>
            <th scope="col" className="text-left p-2">{t('questRank.member.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {questRanks.map((questRank) => (
          <tr key={questRank.id} className="odd:bg-gray-100">
            <td className="p-2">{questRank.id}</td>
            <td className="p-2">{questRank.rank}</td>
            <td className="p-2">{questRank.completedAt}</td>
            <td className="p-2">{questRank.createdAt}</td>
            <td className="p-2">{questRank.updatedAt}</td>
            <td className="p-2">{questRank.deletedAt}</td>
            <td className="p-2">{questRank.quest}</td>
            <td className="p-2">{questRank.member}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/questRanks/edit/' + questRank.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('questRank.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(questRank.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('questRank.list.delete')}</button>
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
