import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useParams } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { MemberQuestDTO } from 'app/member-quest/member-quest-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    status: yup.string().emptyToNull().max(255).required(),
    answer: yup.string().emptyToNull().max(255).required(),
    isRecognized: yup.string().emptyToNull().max(255).required(),
    createdAt: yup.string().emptyToNull().offsetDateTime(),
    completedAt: yup.string().emptyToNull().offsetDateTime(),
    updatedAt: yup.string().emptyToNull().offsetDateTime(),
    deletedAt: yup.string().emptyToNull().offsetDateTime(),
    member: yup.number().integer().emptyToNull(),
    quest: yup.number().integer().emptyToNull()
  });
}

export default function MemberQuestEdit() {
  const { t } = useTranslation();
  useDocumentTitle(t('memberQuest.edit.headline'));

  const navigate = useNavigate();
  const [memberValues, setMemberValues] = useState<Map<number,string>>(new Map());
  const [questValues, setQuestValues] = useState<Map<number,string>>(new Map());
  const params = useParams();
  const currentId = +params.id!;

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareForm = async () => {
    try {
      const memberValuesResponse = await axios.get('/memberQuests/memberValues');
      setMemberValues(memberValuesResponse.data);
      const questValuesResponse = await axios.get('/memberQuests/questValues');
      setQuestValues(questValuesResponse.data);
      const data = (await axios.get('/memberQuests/' + currentId)).data;
      useFormResult.reset(data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareForm();
  }, []);

  const updateMemberQuest = async (data: MemberQuestDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.put('/memberQuests/' + currentId, data);
      navigate('/memberQuests', {
            state: {
              msgSuccess: t('memberQuest.update.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('memberQuest.edit.headline')}</h1>
      <div>
        <Link to="/memberQuests" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('memberQuest.edit.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(updateMemberQuest)} noValidate>
      <InputRow useFormResult={useFormResult} object="memberQuest" field="id" disabled={true} type="number" />
      <InputRow useFormResult={useFormResult} object="memberQuest" field="status" required={true} />
      <InputRow useFormResult={useFormResult} object="memberQuest" field="answer" required={true} />
      <InputRow useFormResult={useFormResult} object="memberQuest" field="isRecognized" required={true} />
      <InputRow useFormResult={useFormResult} object="memberQuest" field="createdAt" />
      <InputRow useFormResult={useFormResult} object="memberQuest" field="completedAt" />
      <InputRow useFormResult={useFormResult} object="memberQuest" field="updatedAt" />
      <InputRow useFormResult={useFormResult} object="memberQuest" field="deletedAt" />
      <InputRow useFormResult={useFormResult} object="memberQuest" field="member" type="select" options={memberValues} />
      <InputRow useFormResult={useFormResult} object="memberQuest" field="quest" type="select" options={questValues} />
      <input type="submit" value={t('memberQuest.edit.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
