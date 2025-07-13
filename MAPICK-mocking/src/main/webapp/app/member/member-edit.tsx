import React, { useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useParams } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { MemberDTO } from 'app/member/member-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    name: yup.string().emptyToNull().max(255),
    nickname: yup.string().emptyToNull().max(255).required(),
    email: yup.string().emptyToNull().max(255),
    password: yup.string().emptyToNull().max(255),
    loginType: yup.string().emptyToNull().max(255).required(),
    provider: yup.string().emptyToNull().max(255),
    role: yup.string().emptyToNull().max(255).required(),
    status: yup.string().emptyToNull().max(255),
    profileImage: yup.string().emptyToNull().max(255),
    lastLogin: yup.string().emptyToNull().offsetDateTime(),
    createdAt: yup.string().emptyToNull().offsetDateTime().required(),
    updatedAt: yup.string().emptyToNull().offsetDateTime(),
    deletedAt: yup.string().emptyToNull().offsetDateTime()
  });
}

export default function MemberEdit() {
  const { t } = useTranslation();
  useDocumentTitle(t('member.edit.headline'));

  const navigate = useNavigate();
  const params = useParams();
  const currentId = +params.id!;

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const getMessage = (key: string) => {
    const messages: Record<string, string> = {
      MEMBER_EMAIL_UNIQUE: t('exists.member.email')
    };
    return messages[key];
  };

  const prepareForm = async () => {
    try {
      const data = (await axios.get('/members/' + currentId)).data;
      useFormResult.reset(data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareForm();
  }, []);

  const updateMember = async (data: MemberDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.put('/members/' + currentId, data);
      navigate('/members', {
            state: {
              msgSuccess: t('member.update.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t, getMessage);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('member.edit.headline')}</h1>
      <div>
        <Link to="/members" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('member.edit.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(updateMember)} noValidate>
      <input type="submit" value={t('member.edit.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6 mb-5" />
      <InputRow useFormResult={useFormResult} object="member" field="id" disabled={true} type="number" />
      <InputRow useFormResult={useFormResult} object="member" field="name" />
      <InputRow useFormResult={useFormResult} object="member" field="nickname" required={true} />
      <InputRow useFormResult={useFormResult} object="member" field="email" />
      <InputRow useFormResult={useFormResult} object="member" field="password" />
      <InputRow useFormResult={useFormResult} object="member" field="loginType" required={true} />
      <InputRow useFormResult={useFormResult} object="member" field="provider" />
      <InputRow useFormResult={useFormResult} object="member" field="role" required={true} />
      <InputRow useFormResult={useFormResult} object="member" field="status" />
      <InputRow useFormResult={useFormResult} object="member" field="profileImage" />
      <InputRow useFormResult={useFormResult} object="member" field="lastLogin" />
      <InputRow useFormResult={useFormResult} object="member" field="createdAt" required={true} />
      <InputRow useFormResult={useFormResult} object="member" field="updatedAt" />
      <InputRow useFormResult={useFormResult} object="member" field="deletedAt" />
      <input type="submit" value={t('member.edit.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
