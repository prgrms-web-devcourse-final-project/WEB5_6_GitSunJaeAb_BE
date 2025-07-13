import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useParams } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { MemberQuestEvidenceDTO } from 'app/member-quest-evidence/member-quest-evidence-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    imageUrl: yup.string().emptyToNull().max(255).required(),
    description: yup.string().emptyToNull(),
    createdAt: yup.string().emptyToNull().offsetDateTime(),
    updatedAt: yup.string().emptyToNull().offsetDateTime(),
    deletedAt: yup.string().emptyToNull().offsetDateTime(),
    memberQuest: yup.number().integer().emptyToNull()
  });
}

export default function MemberQuestEvidenceEdit() {
  const { t } = useTranslation();
  useDocumentTitle(t('memberQuestEvidence.edit.headline'));

  const navigate = useNavigate();
  const [memberQuestValues, setMemberQuestValues] = useState<Map<number,string>>(new Map());
  const params = useParams();
  const currentId = +params.id!;

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareForm = async () => {
    try {
      const memberQuestValuesResponse = await axios.get('/memberQuestEvidences/memberQuestValues');
      setMemberQuestValues(memberQuestValuesResponse.data);
      const data = (await axios.get('/memberQuestEvidences/' + currentId)).data;
      useFormResult.reset(data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareForm();
  }, []);

  const updateMemberQuestEvidence = async (data: MemberQuestEvidenceDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.put('/memberQuestEvidences/' + currentId, data);
      navigate('/memberQuestEvidences', {
            state: {
              msgSuccess: t('memberQuestEvidence.update.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('memberQuestEvidence.edit.headline')}</h1>
      <div>
        <Link to="/memberQuestEvidences" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('memberQuestEvidence.edit.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(updateMemberQuestEvidence)} noValidate>
      <InputRow useFormResult={useFormResult} object="memberQuestEvidence" field="id" disabled={true} type="number" />
      <InputRow useFormResult={useFormResult} object="memberQuestEvidence" field="imageUrl" required={true} />
      <InputRow useFormResult={useFormResult} object="memberQuestEvidence" field="description" type="textarea" />
      <InputRow useFormResult={useFormResult} object="memberQuestEvidence" field="createdAt" />
      <InputRow useFormResult={useFormResult} object="memberQuestEvidence" field="updatedAt" />
      <InputRow useFormResult={useFormResult} object="memberQuestEvidence" field="deletedAt" />
      <InputRow useFormResult={useFormResult} object="memberQuestEvidence" field="memberQuest" type="select" options={memberQuestValues} />
      <input type="submit" value={t('memberQuestEvidence.edit.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
