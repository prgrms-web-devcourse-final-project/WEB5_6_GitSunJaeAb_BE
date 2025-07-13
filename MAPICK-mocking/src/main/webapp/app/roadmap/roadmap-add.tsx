import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { RoadmapDTO } from 'app/roadmap/roadmap-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    title: yup.string().emptyToNull().max(255).required(),
    description: yup.string().emptyToNull(),
    thumbnail: yup.string().emptyToNull().max(255),
    isPublic: yup.bool(),
    isAnimated: yup.bool(),
    likeCount: yup.number().integer().emptyToNull(),
    viewCount: yup.number().integer().emptyToNull(),
    roadmapType: yup.string().emptyToNull().max(255).required(),
    createdAt: yup.string().emptyToNull().offsetDateTime().required(),
    updatedAt: yup.string().emptyToNull().offsetDateTime(),
    deletedAt: yup.string().emptyToNull().offsetDateTime(),
    member: yup.number().integer().emptyToNull(),
    originalRoadmap: yup.number().integer().emptyToNull()
  });
}

export default function RoadmapAdd() {
  const { t } = useTranslation();
  useDocumentTitle(t('roadmap.add.headline'));

  const navigate = useNavigate();
  const [memberValues, setMemberValues] = useState<Map<number,string>>(new Map());
  const [originalRoadmapValues, setOriginalRoadmapValues] = useState<Map<number,string>>(new Map());

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareRelations = async () => {
    try {
      const memberValuesResponse = await axios.get('/roadmaps/memberValues');
      setMemberValues(memberValuesResponse.data);
      const originalRoadmapValuesResponse = await axios.get('/roadmaps/originalRoadmapValues');
      setOriginalRoadmapValues(originalRoadmapValuesResponse.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareRelations();
  }, []);

  const createRoadmap = async (data: RoadmapDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.post('/roadmaps', data);
      navigate('/roadmaps', {
            state: {
              msgSuccess: t('roadmap.create.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('roadmap.add.headline')}</h1>
      <div>
        <Link to="/roadmaps" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('roadmap.add.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(createRoadmap)} noValidate>
      <InputRow useFormResult={useFormResult} object="roadmap" field="title" required={true} />
      <InputRow useFormResult={useFormResult} object="roadmap" field="description" type="textarea" />
      <InputRow useFormResult={useFormResult} object="roadmap" field="thumbnail" />
      <InputRow useFormResult={useFormResult} object="roadmap" field="isPublic" type="checkbox" />
      <InputRow useFormResult={useFormResult} object="roadmap" field="isAnimated" type="checkbox" />
      <InputRow useFormResult={useFormResult} object="roadmap" field="likeCount" type="number" />
      <InputRow useFormResult={useFormResult} object="roadmap" field="viewCount" type="number" />
      <InputRow useFormResult={useFormResult} object="roadmap" field="roadroadmapType" required={true} />
      <InputRow useFormResult={useFormResult} object="roadmap" field="createdAt" required={true} />
      <InputRow useFormResult={useFormResult} object="roadmap" field="updatedAt" />
      <InputRow useFormResult={useFormResult} object="roadmap" field="deletedAt" />
      <InputRow useFormResult={useFormResult} object="roadmap" field="member" type="select" options={memberValues} />
      <InputRow useFormResult={useFormResult} object="roadmap" field="originalRoadmap" type="select" options={originalRoadmapValues} />
      <input type="submit" value={t('roadmap.add.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
