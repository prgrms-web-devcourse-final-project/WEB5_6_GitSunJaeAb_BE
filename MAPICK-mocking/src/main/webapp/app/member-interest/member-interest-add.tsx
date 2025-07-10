import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { MemberInterestDTO } from 'app/member-interest/member-interest-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    createdAt: yup.string().emptyToNull().offsetDateTime().required(),
    interest: yup.number().integer().emptyToNull(),
    member: yup.number().integer().emptyToNull()
  });
}

export default function MemberInterestAdd() {
  const { t } = useTranslation();
  useDocumentTitle(t('memberInterest.add.headline'));

  const navigate = useNavigate();
  const [interestValues, setInterestValues] = useState<Map<number,string>>(new Map());
  const [memberValues, setMemberValues] = useState<Map<number,string>>(new Map());

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareRelations = async () => {
    try {
      const interestValuesResponse = await axios.get('/api/memberInterests/interestValues');
      setInterestValues(interestValuesResponse.data);
      const memberValuesResponse = await axios.get('/api/memberInterests/memberValues');
      setMemberValues(memberValuesResponse.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareRelations();
  }, []);

  const createMemberInterest = async (data: MemberInterestDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.post('/api/memberInterests', data);
      navigate('/memberInterests', {
            state: {
              msgSuccess: t('memberInterest.create.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('memberInterest.add.headline')}</h1>
      <div>
        <Link to="/memberInterests" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('memberInterest.add.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(createMemberInterest)} noValidate>
      <InputRow useFormResult={useFormResult} object="memberInterest" field="createdAt" required={true} />
      <InputRow useFormResult={useFormResult} object="memberInterest" field="interest" type="select" options={interestValues} />
      <InputRow useFormResult={useFormResult} object="memberInterest" field="member" type="select" options={memberValues} />
      <input type="submit" value={t('memberInterest.add.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
