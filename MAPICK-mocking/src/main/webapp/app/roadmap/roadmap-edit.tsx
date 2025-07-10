import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useParams } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { MapDTO } from 'app/roadmap/roadmap-model';
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
    mapType: yup.string().emptyToNull().max(255).required(),
    createdAt: yup.string().emptyToNull().offsetDateTime().required(),
    updatedAt: yup.string().emptyToNull().offsetDateTime(),
    deletedAt: yup.string().emptyToNull().offsetDateTime(),
    member: yup.number().integer().emptyToNull(),
    originalMap: yup.number().integer().emptyToNull()
  });
}

export default function RoadmapEdit() {
  const { t } = useTranslation();
  useDocumentTitle(t('roadmap.edit.headline'));

  const navigate = useNavigate();
  const [memberValues, setMemberValues] = useState<Map<number,string>>(new Map());
  const [originalMapValues, setOriginalMapValues] = useState<Map<number,string>>(new Map());
  const params = useParams();
  const currentId = +params.id!;

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareForm = async () => {
    try {
      const memberValuesResponse = await axios.get('/api/maps/memberValues');
      setMemberValues(memberValuesResponse.data);
      const originalMapValuesResponse = await axios.get('/api/maps/originalMapValues');
      setOriginalMapValues(originalMapValuesResponse.data);
      const data = (await axios.get('/api/maps/' + currentId)).data;
      useFormResult.reset(data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareForm();
  }, []);

  const updateMap = async (data: MapDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.put('/api/maps/' + currentId, data);
      navigate('/maps', {
            state: {
              msgSuccess: t('roadmap.update.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('roadmap.edit.headline')}</h1>
      <div>
        <Link to="/maps" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('roadmap.edit.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(updateMap)} noValidate>
      <input type="submit" value={t('roadmap.edit.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6 mb-5" />
      <InputRow useFormResult={useFormResult} object="roadmap" field="id" disabled={true} type="number" />
      <InputRow useFormResult={useFormResult} object="roadmap" field="title" required={true} />
      <InputRow useFormResult={useFormResult} object="roadmap" field="description" type="textarea" />
      <InputRow useFormResult={useFormResult} object="roadmap" field="thumbnail" />
      <InputRow useFormResult={useFormResult} object="roadmap" field="isPublic" type="checkbox" />
      <InputRow useFormResult={useFormResult} object="roadmap" field="isAnimated" type="checkbox" />
      <InputRow useFormResult={useFormResult} object="roadmap" field="likeCount" type="number" />
      <InputRow useFormResult={useFormResult} object="roadmap" field="viewCount" type="number" />
      <InputRow useFormResult={useFormResult} object="roadmap" field="mapType" required={true} />
      <InputRow useFormResult={useFormResult} object="roadmap" field="createdAt" required={true} />
      <InputRow useFormResult={useFormResult} object="roadmap" field="updatedAt" />
      <InputRow useFormResult={useFormResult} object="roadmap" field="deletedAt" />
      <InputRow useFormResult={useFormResult} object="roadmap" field="member" type="select" options={memberValues} />
      <InputRow useFormResult={useFormResult} object="roadmap" field="originalMap" type="select" options={originalMapValues} />
      <input type="submit" value={t('roadmap.edit.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
