import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { QuestDTO } from 'app/quest/quest-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    title: yup.string().emptyToNull().max(255).required(),
    questImage: yup.string().emptyToNull().max(255).required(),
    description: yup.string().emptyToNull(),
    isActive: yup.bool(),
    createdAt: yup.string().emptyToNull().offsetDateTime(),
    completedAt: yup.string().emptyToNull().offsetDateTime(),
    updatedAt: yup.string().emptyToNull().offsetDateTime(),
    deletedAt: yup.string().emptyToNull().offsetDateTime(),
    member: yup.number().integer().emptyToNull()
  });
}

export default function QuestAdd() {
  const { t } = useTranslation();
  useDocumentTitle(t('quest.add.headline'));

  const navigate = useNavigate();
  const [memberValues, setMemberValues] = useState<Map<number,string>>(new Map());

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareRelations = async () => {
    try {
      const memberValuesResponse = await axios.get('/quests/memberValues');
      setMemberValues(memberValuesResponse.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareRelations();
  }, []);

  const createQuest = async (data: QuestDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.post('/quests', data);
      navigate('/quests', {
            state: {
              msgSuccess: t('quest.create.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('quest.add.headline')}</h1>
      <div>
        <Link to="/quests" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('quest.add.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(createQuest)} noValidate>
      <InputRow useFormResult={useFormResult} object="quest" field="title" required={true} />
      <InputRow useFormResult={useFormResult} object="quest" field="questImage" required={true} />
      <InputRow useFormResult={useFormResult} object="quest" field="description" type="textarea" />
      <InputRow useFormResult={useFormResult} object="quest" field="isActive" type="checkbox" />
      <InputRow useFormResult={useFormResult} object="quest" field="createdAt" />
      <InputRow useFormResult={useFormResult} object="quest" field="completedAt" />
      <InputRow useFormResult={useFormResult} object="quest" field="updatedAt" />
      <InputRow useFormResult={useFormResult} object="quest" field="deletedAt" />
      <InputRow useFormResult={useFormResult} object="quest" field="member" type="select" options={memberValues} />
      <input type="submit" value={t('quest.add.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
