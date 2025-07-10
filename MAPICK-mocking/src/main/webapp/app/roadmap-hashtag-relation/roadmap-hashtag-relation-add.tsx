import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { MapHashtagRelationDTO } from 'app/roadmap-hashtag-relation/roadmap-hashtag-relation-model';
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

export default function RoadmapHashtagRelationAdd() {
  const { t } = useTranslation();
  useDocumentTitle(t('roadmapHashtagRelation.add.headline'));

  const navigate = useNavigate();
  const [hashtagValues, setHashtagValues] = useState<Map<number,string>>(new Map());
  const [mapValues, setMapValues] = useState<Map<number,string>>(new Map());

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareRelations = async () => {
    try {
      const hashtagValuesResponse = await axios.get('/api/roadmapHashtagRelations/hashtagValues');
      setHashtagValues(hashtagValuesResponse.data);
      const mapValuesResponse = await axios.get('/api/roadmapHashtagRelations/mapValues');
      setMapValues(mapValuesResponse.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareRelations();
  }, []);

  const createMapHashtagRelation = async (data: MapHashtagRelationDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.post('/api/roadmapHashtagRelations', data);
      navigate('/roadmapHashtagRelations', {
            state: {
              msgSuccess: t('roadmapHashtagRelation.create.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('roadmapHashtagRelation.add.headline')}</h1>
      <div>
        <Link to="/roadmapHashtagRelations" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('roadmapHashtagRelation.add.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(createMapHashtagRelation)} noValidate>
      <InputRow useFormResult={useFormResult} object="roadmapHashtagRelation" field="createdAt" />
      <InputRow useFormResult={useFormResult} object="roadmapHashtagRelation" field="hashtag" type="select" options={hashtagValues} />
      <InputRow useFormResult={useFormResult} object="roadmapHashtagRelation" field="roadmap" type="select" options={mapValues} />
      <input type="submit" value={t('roadmapHashtagRelation.add.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
