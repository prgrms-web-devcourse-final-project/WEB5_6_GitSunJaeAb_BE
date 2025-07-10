import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { MarkerDTO } from 'app/marker/marker-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    title: yup.string().emptyToNull().max(255),
    description: yup.string().emptyToNull(),
    lat: yup.number().emptyToNull().required(),
    lng: yup.number().emptyToNull().required(),
    color: yup.string().emptyToNull().max(255),
    imageUrl: yup.string().emptyToNull().max(255),
    markerSeq: yup.number().integer().emptyToNull(),
    createdAt: yup.string().emptyToNull().offsetDateTime().required(),
    updatedAt: yup.string().emptyToNull().offsetDateTime(),
    deletedAt: yup.string().emptyToNull().offsetDateTime(),
    member: yup.number().integer().emptyToNull(),
    layer: yup.number().integer().emptyToNull()
  });
}

export default function MarkerAdd() {
  const { t } = useTranslation();
  useDocumentTitle(t('marker.add.headline'));

  const navigate = useNavigate();
  const [memberValues, setMemberValues] = useState<Map<number,string>>(new Map());
  const [layerValues, setLayerValues] = useState<Map<number,string>>(new Map());

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareRelations = async () => {
    try {
      const memberValuesResponse = await axios.get('/api/markers/memberValues');
      setMemberValues(memberValuesResponse.data);
      const layerValuesResponse = await axios.get('/api/markers/layerValues');
      setLayerValues(layerValuesResponse.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareRelations();
  }, []);

  const createMarker = async (data: MarkerDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.post('/api/markers', data);
      navigate('/markers', {
            state: {
              msgSuccess: t('marker.create.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('marker.add.headline')}</h1>
      <div>
        <Link to="/markers" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('marker.add.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(createMarker)} noValidate>
      <InputRow useFormResult={useFormResult} object="marker" field="title" />
      <InputRow useFormResult={useFormResult} object="marker" field="description" type="textarea" />
      <InputRow useFormResult={useFormResult} object="marker" field="lat" required={true} />
      <InputRow useFormResult={useFormResult} object="marker" field="lng" required={true} />
      <InputRow useFormResult={useFormResult} object="marker" field="color" />
      <InputRow useFormResult={useFormResult} object="marker" field="imageUrl" />
      <InputRow useFormResult={useFormResult} object="marker" field="markerSeq" type="number" />
      <InputRow useFormResult={useFormResult} object="marker" field="createdAt" required={true} />
      <InputRow useFormResult={useFormResult} object="marker" field="updatedAt" />
      <InputRow useFormResult={useFormResult} object="marker" field="deletedAt" />
      <InputRow useFormResult={useFormResult} object="marker" field="member" type="select" options={memberValues} />
      <InputRow useFormResult={useFormResult} object="marker" field="layer" type="select" options={layerValues} />
      <input type="submit" value={t('marker.add.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
