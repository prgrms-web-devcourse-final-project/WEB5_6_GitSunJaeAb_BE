import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { MapHashtagRelationDTO } from 'app/map-hashtag-relation/map-hashtag-relation-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    createdAt: yup.string().emptyToNull().offsetDateTime(),
    hashtag: yup.number().integer().emptyToNull(),
    map: yup.number().integer().emptyToNull()
  });
}

export default function MapHashtagRelationAdd() {
  const { t } = useTranslation();
  useDocumentTitle(t('mapHashtagRelation.add.headline'));

  const navigate = useNavigate();
  const [hashtagValues, setHashtagValues] = useState<Map<number,string>>(new Map());
  const [mapValues, setMapValues] = useState<Map<number,string>>(new Map());

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareRelations = async () => {
    try {
      const hashtagValuesResponse = await axios.get('/api/mapHashtagRelations/hashtagValues');
      setHashtagValues(hashtagValuesResponse.data);
      const mapValuesResponse = await axios.get('/api/mapHashtagRelations/mapValues');
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
      await axios.post('/api/mapHashtagRelations', data);
      navigate('/mapHashtagRelations', {
            state: {
              msgSuccess: t('mapHashtagRelation.create.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('mapHashtagRelation.add.headline')}</h1>
      <div>
        <Link to="/mapHashtagRelations" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('mapHashtagRelation.add.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(createMapHashtagRelation)} noValidate>
      <InputRow useFormResult={useFormResult} object="mapHashtagRelation" field="createdAt" />
      <InputRow useFormResult={useFormResult} object="mapHashtagRelation" field="hashtag" type="select" options={hashtagValues} />
      <InputRow useFormResult={useFormResult} object="mapHashtagRelation" field="map" type="select" options={mapValues} />
      <input type="submit" value={t('mapHashtagRelation.add.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
