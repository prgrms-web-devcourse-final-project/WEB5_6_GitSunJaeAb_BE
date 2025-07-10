import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError } from 'app/common/utils';
import { MemberInterestDTO } from 'app/member-interest/member-interest-model';
import axios from 'axios';
import useDocumentTitle from 'app/common/use-document-title';


export default function MemberInterestList() {
  const { t } = useTranslation();
  useDocumentTitle(t('memberInterest.list.headline'));

  const [memberInterests, setMemberInterests] = useState<MemberInterestDTO[]>([]);
  const navigate = useNavigate();

  const getAllMemberInterests = async () => {
    try {
      const response = await axios.get('/api/memberInterests');
      setMemberInterests(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/memberInterests/' + id);
      navigate('/memberInterests', {
            state: {
              msgInfo: t('memberInterest.delete.success')
            }
          });
      getAllMemberInterests();
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    getAllMemberInterests();
  }, []);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('memberInterest.list.headline')}</h1>
      <div>
        <Link to="/memberInterests/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('memberInterest.list.createNew')}</Link>
      </div>
    </div>
    {!memberInterests || memberInterests.length === 0 ? (
    <div>{t('memberInterest.list.empty')}</div>
    ) : (
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('memberInterest.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('memberInterest.createdAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('memberInterest.interest.label')}</th>
            <th scope="col" className="text-left p-2">{t('memberInterest.member.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {memberInterests.map((memberInterest) => (
          <tr key={memberInterest.id} className="odd:bg-gray-100">
            <td className="p-2">{memberInterest.id}</td>
            <td className="p-2">{memberInterest.createdAt}</td>
            <td className="p-2">{memberInterest.interest}</td>
            <td className="p-2">{memberInterest.member}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/memberInterests/edit/' + memberInterest.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('memberInterest.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(memberInterest.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('memberInterest.list.delete')}</button>
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
