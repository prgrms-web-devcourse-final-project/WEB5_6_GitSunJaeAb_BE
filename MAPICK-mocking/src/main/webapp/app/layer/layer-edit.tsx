import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useParams } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { LayerDTO } from 'app/layer/layer-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    name: yup.string().emptyToNull().max(255).required(),
    description: yup.string().emptyToNull(),
    layerSeq: yup.number().integer().emptyToNull(),
    layerTime: yup.string().emptyToNull(),
    createdAt: yup.string().emptyToNull().offsetDateTime().required(),
    updatedAt: yup.string().emptyToNull().offsetDateTime(),
    deletedAt: yup.string().emptyToNull().offsetDateTime(),
    member: yup.number().integer().emptyToNull(),
    roadmap: yup.number().integer().emptyToNull()
  });
}

export default function LayerEdit() {
  const { t } = useTranslation();
  useDocumentTitle(t('layer.edit.headline'));

  const navigate = useNavigate();
  const [memberValues, setMemberValues] = useState<Map<number,string>>(new Map());
  const [mapValues, setMapValues] = useState<Map<number,string>>(new Map());
  const params = useParams();
  const currentId = +params.id!;

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareForm = async () => {
    try {
      const memberValuesResponse = await axios.get('/api/layers/memberValues');
      setMemberValues(memberValuesResponse.data);
      const mapValuesResponse = await axios.get('/api/layers/mapValues');
      setMapValues(mapValuesResponse.data);
      const data = (await axios.get('/api/layers/' + currentId)).data;
      useFormResult.reset(data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareForm();
  }, []);

  const updateLayer = async (data: LayerDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.put('/api/layers/' + currentId, data);
      navigate('/layers', {
            state: {
              msgSuccess: t('layer.update.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('layer.edit.headline')}</h1>
      <div>
        <Link to="/layers" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('layer.edit.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(updateLayer)} noValidate>
      <InputRow useFormResult={useFormResult} object="layer" field="id" disabled={true} type="number" />
      <InputRow useFormResult={useFormResult} object="layer" field="name" required={true} />
      <InputRow useFormResult={useFormResult} object="layer" field="description" type="textarea" />
      <InputRow useFormResult={useFormResult} object="layer" field="layerSeq" type="number" />
      <InputRow useFormResult={useFormResult} object="layer" field="layerTime" type="datepicker" />
      <InputRow useFormResult={useFormResult} object="layer" field="createdAt" required={true} />
      <InputRow useFormResult={useFormResult} object="layer" field="updatedAt" />
      <InputRow useFormResult={useFormResult} object="layer" field="deletedAt" />
      <InputRow useFormResult={useFormResult} object="layer" field="member" type="select" options={memberValues} />
      <InputRow useFormResult={useFormResult} object="layer" field="roadmap" type="select" options={mapValues} />
      <input type="submit" value={t('layer.edit.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
