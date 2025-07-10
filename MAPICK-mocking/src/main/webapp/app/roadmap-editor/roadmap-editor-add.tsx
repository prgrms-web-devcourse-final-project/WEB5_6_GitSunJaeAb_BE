import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { MapEditorDTO } from 'app/roadmap-editor/roadmap-editor-model';
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

export default function RoadmapEditorAdd() {
  const { t } = useTranslation();
  useDocumentTitle(t('roadmapEditor.add.headline'));

  const navigate = useNavigate();
  const [mapValues, setMapValues] = useState<Map<number,string>>(new Map());
  const [memberValues, setMemberValues] = useState<Map<number,string>>(new Map());
  const [invitedByValues, setInvitedByValues] = useState<Map<number,string>>(new Map());

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareRelations = async () => {
    try {
      const mapValuesResponse = await axios.get('/api/roadmapEditors/mapValues');
      setMapValues(mapValuesResponse.data);
      const memberValuesResponse = await axios.get('/api/roadmapEditors/memberValues');
      setMemberValues(memberValuesResponse.data);
      const invitedByValuesResponse = await axios.get('/api/roadmapEditors/invitedByValues');
      setInvitedByValues(invitedByValuesResponse.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareRelations();
  }, []);

  const createMapEditor = async (data: MapEditorDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.post('/api/roadmapEditors', data);
      navigate('/roadmapEditors', {
            state: {
              msgSuccess: t('roadmapEditor.create.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('roadmapEditor.add.headline')}</h1>
      <div>
        <Link to="/roadmapEditors" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('roadmapEditor.add.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(createMapEditor)} noValidate>
      <InputRow useFormResult={useFormResult} object="roadmapEditor" field="permission" required={true} />
      <InputRow useFormResult={useFormResult} object="roadmapEditor" field="createdAt" required={true} />
      <InputRow useFormResult={useFormResult} object="roadmapEditor" field="updatedAt" />
      <InputRow useFormResult={useFormResult} object="roadmapEditor" field="deletedAt" />
      <InputRow useFormResult={useFormResult} object="roadmapEditor" field="roadmap" type="select" options={mapValues} />
      <InputRow useFormResult={useFormResult} object="roadmapEditor" field="member" type="select" options={memberValues} />
      <InputRow useFormResult={useFormResult} object="roadmapEditor" field="invitedBy" type="select" options={invitedByValues} />
      <input type="submit" value={t('roadmapEditor.add.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
