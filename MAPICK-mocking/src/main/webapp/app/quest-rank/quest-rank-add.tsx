import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { QuestRankDTO } from 'app/quest-rank/quest-rank-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    rank: yup.number().integer().emptyToNull().required(),
    completedAt: yup.string().emptyToNull().offsetDateTime(),
    createdAt: yup.string().emptyToNull().offsetDateTime(),
    updatedAt: yup.string().emptyToNull().offsetDateTime(),
    deletedAt: yup.string().emptyToNull().offsetDateTime(),
    quest: yup.number().integer().emptyToNull(),
    member: yup.number().integer().emptyToNull()
  });
}

export default function QuestRankAdd() {
  const { t } = useTranslation();
  useDocumentTitle(t('questRank.add.headline'));

  const navigate = useNavigate();
  const [questValues, setQuestValues] = useState<Map<number,string>>(new Map());
  const [memberValues, setMemberValues] = useState<Map<number,string>>(new Map());

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareRelations = async () => {
    try {
      const questValuesResponse = await axios.get('/questRanks/questValues');
      setQuestValues(questValuesResponse.data);
      const memberValuesResponse = await axios.get('/questRanks/memberValues');
      setMemberValues(memberValuesResponse.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareRelations();
  }, []);

  const createQuestRank = async (data: QuestRankDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.post('/questRanks', data);
      navigate('/questRanks', {
            state: {
              msgSuccess: t('questRank.create.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('questRank.add.headline')}</h1>
      <div>
        <Link to="/questRanks" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('questRank.add.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(createQuestRank)} noValidate>
      <InputRow useFormResult={useFormResult} object="questRank" field="rank" required={true} type="number" />
      <InputRow useFormResult={useFormResult} object="questRank" field="completedAt" />
      <InputRow useFormResult={useFormResult} object="questRank" field="createdAt" />
      <InputRow useFormResult={useFormResult} object="questRank" field="updatedAt" />
      <InputRow useFormResult={useFormResult} object="questRank" field="deletedAt" />
      <InputRow useFormResult={useFormResult} object="questRank" field="quest" type="select" options={questValues} />
      <InputRow useFormResult={useFormResult} object="questRank" field="member" type="select" options={memberValues} />
      <input type="submit" value={t('questRank.add.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
