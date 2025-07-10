import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError } from 'app/common/utils';
import { MemberDTO } from 'app/member/member-model';
import axios from 'axios';
import useDocumentTitle from 'app/common/use-document-title';


export default function MemberList() {
  const { t } = useTranslation();
  useDocumentTitle(t('member.list.headline'));

  const [members, setMembers] = useState<MemberDTO[]>([]);
  const navigate = useNavigate();

  const getAllMembers = async () => {
    try {
      const response = await axios.get('/api/members');
      setMembers(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/members/' + id);
      navigate('/members', {
            state: {
              msgInfo: t('member.delete.success')
            }
          });
      getAllMembers();
    } catch (error: any) {
      if (error?.response?.data?.code === 'REFERENCED') {
        const messageParts = error.response.data.message.split(',');
        navigate('/members', {
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
    getAllMembers();
  }, []);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('member.list.headline')}</h1>
      <div>
        <Link to="/members/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('member.list.createNew')}</Link>
      </div>
    </div>
    {!members || members.length === 0 ? (
    <div>{t('member.list.empty')}</div>
    ) : (
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('member.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('member.name.label')}</th>
            <th scope="col" className="text-left p-2">{t('member.nickname.label')}</th>
            <th scope="col" className="text-left p-2">{t('member.email.label')}</th>
            <th scope="col" className="text-left p-2">{t('member.password.label')}</th>
            <th scope="col" className="text-left p-2">{t('member.loginType.label')}</th>
            <th scope="col" className="text-left p-2">{t('member.provider.label')}</th>
            <th scope="col" className="text-left p-2">{t('member.role.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {members.map((member) => (
          <tr key={member.id} className="odd:bg-gray-100">
            <td className="p-2">{member.id}</td>
            <td className="p-2">{member.name}</td>
            <td className="p-2">{member.nickname}</td>
            <td className="p-2">{member.email}</td>
            <td className="p-2">{member.password}</td>
            <td className="p-2">{member.loginType}</td>
            <td className="p-2">{member.provider}</td>
            <td className="p-2">{member.role}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/members/edit/' + member.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('member.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(member.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('member.list.delete')}</button>
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
