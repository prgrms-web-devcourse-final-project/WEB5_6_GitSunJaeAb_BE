import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useParams } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { RoadmapHashtagRelationDTO } from 'app/roadmap-hashtag-relation/roadmap-hashtag-relation-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    createdAt: yup.string().emptyToNull().offsetDateTime(),
    hashtag: yup.number().integer().emptyToNull(),
    roadmap: yup.number().integer().emptyToNull()
  });
}

export default function RoadmapHashtagRelationEdit() {
  const { t } = useTranslation();
  useDocumentTitle(t('roadmapHashtagRelation.edit.headline'));

  const navigate = useNavigate();
  const [hashtagValues, setHashtagValues] = useState<Map<number,string>>(new Map());
  const [roadmapValues, setRoadmapValues] = useState<Map<number,string>>(new Map());
  const params = useParams();
  const currentId = +params.id!;

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareForm = async () => {
    try {
      const hashtagValuesResponse = await axios.get('/roadmapHashtagRelations/hashtagValues');
      setHashtagValues(hashtagValuesResponse.data);
      const roadmapValuesResponse = await axios.get('/roadmapHashtagRelations/roadmapValues');
      setRoadmapValues(roadmapValuesResponse.data);
      const data = (await axios.get('/roadmapHashtagRelations/' + currentId)).data;
      useFormResult.reset(data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareForm();
  }, []);

  const updateRoadmapHashtagRelation = async (data: RoadmapHashtagRelationDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.put('/roadmapHashtagRelations/' + currentId, data);
      navigate('/roadmapHashtagRelations', {
            state: {
              msgSuccess: t('roadmapHashtagRelation.update.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('roadmapHashtagRelation.edit.headline')}</h1>
      <div>
        <Link to="/roadmapHashtagRelations" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('roadmapHashtagRelation.edit.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(updateRoadmapHashtagRelation)} noValidate>
      <InputRow useFormResult={useFormResult} object="roadmapHashtagRelation" field="id" disabled={true} type="number" />
      <InputRow useFormResult={useFormResult} object="roadmapHashtagRelation" field="createdAt" />
      <InputRow useFormResult={useFormResult} object="roadmapHashtagRelation" field="hashtag" type="select" options={hashtagValues} />
      <InputRow useFormResult={useFormResult} object="roadmapHashtagRelation" field="roadmap" type="select" options={roadmapValues} />
      <input type="submit" value={t('roadmapHashtagRelation.edit.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
