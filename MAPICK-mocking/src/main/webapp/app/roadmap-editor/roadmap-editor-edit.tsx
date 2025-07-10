import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useParams } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { RoadmapEditorDTO } from 'app/roadmap-editor/roadmap-editor-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    permission: yup.string().emptyToNull().max(255).required(),
    createdAt: yup.string().emptyToNull().offsetDateTime().required(),
    updatedAt: yup.string().emptyToNull().offsetDateTime(),
    deletedAt: yup.string().emptyToNull().offsetDateTime(),
    roadmap: yup.number().integer().emptyToNull(),
    member: yup.number().integer().emptyToNull(),
    invitedBy: yup.number().integer().emptyToNull()
  });
}

export default function RoadmapEditorEdit() {
  const { t } = useTranslation();
  useDocumentTitle(t('roadmapEditor.edit.headline'));

  const navigate = useNavigate();
  const [roadmapValues, setRoadmapValues] = useState<Map<number,string>>(new Map());
  const [memberValues, setMemberValues] = useState<Map<number,string>>(new Map());
  const [invitedByValues, setInvitedByValues] = useState<Map<number,string>>(new Map());
  const params = useParams();
  const currentId = +params.id!;

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareForm = async () => {
    try {
      const roadmapValuesResponse = await axios.get('/roadmapEditors/roadmapValues');
      setRoadmapValues(roadmapValuesResponse.data);
      const memberValuesResponse = await axios.get('/roadmapEditors/memberValues');
      setMemberValues(memberValuesResponse.data);
      const invitedByValuesResponse = await axios.get('/roadmapEditors/invitedByValues');
      setInvitedByValues(invitedByValuesResponse.data);
      const data = (await axios.get('/roadmapEditors/' + currentId)).data;
      useFormResult.reset(data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareForm();
  }, []);

  const updateRoadmapEditor = async (data: RoadmapEditorDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.put('/roadmapEditors/' + currentId, data);
      navigate('/roadmapEditors', {
            state: {
              msgSuccess: t('roadmapEditor.update.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('roadmapEditor.edit.headline')}</h1>
      <div>
        <Link to="/roadmapEditors" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('roadmapEditor.edit.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(updateRoadmapEditor)} noValidate>
      <InputRow useFormResult={useFormResult} object="roadmapEditor" field="id" disabled={true} type="number" />
      <InputRow useFormResult={useFormResult} object="roadmapEditor" field="permission" required={true} />
      <InputRow useFormResult={useFormResult} object="roadmapEditor" field="createdAt" required={true} />
      <InputRow useFormResult={useFormResult} object="roadmapEditor" field="updatedAt" />
      <InputRow useFormResult={useFormResult} object="roadmapEditor" field="deletedAt" />
      <InputRow useFormResult={useFormResult} object="roadmapEditor" field="roadmap" type="select" options={roadmapValues} />
      <InputRow useFormResult={useFormResult} object="roadmapEditor" field="member" type="select" options={memberValues} />
      <InputRow useFormResult={useFormResult} object="roadmapEditor" field="invitedBy" type="select" options={invitedByValues} />
      <input type="submit" value={t('roadmapEditor.edit.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
