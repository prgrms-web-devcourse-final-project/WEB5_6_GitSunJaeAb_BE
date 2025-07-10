import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useParams } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { MapCategoryRelationDTO } from 'app/map-category-relation/map-category-relation-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    createdAt: yup.string().emptyToNull().offsetDateTime(),
    map: yup.number().integer().emptyToNull()
  });
}

export default function MapCategoryRelationEdit() {
  const { t } = useTranslation();
  useDocumentTitle(t('mapCategoryRelation.edit.headline'));

  const navigate = useNavigate();
  const [mapValues, setMapValues] = useState<Map<number,string>>(new Map());
  const params = useParams();
  const currentId = +params.id!;

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareForm = async () => {
    try {
      const mapValuesResponse = await axios.get('/api/mapCategoryRelations/mapValues');
      setMapValues(mapValuesResponse.data);
      const data = (await axios.get('/api/mapCategoryRelations/' + currentId)).data;
      useFormResult.reset(data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareForm();
  }, []);

  const updateMapCategoryRelation = async (data: MapCategoryRelationDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.put('/api/mapCategoryRelations/' + currentId, data);
      navigate('/mapCategoryRelations', {
            state: {
              msgSuccess: t('mapCategoryRelation.update.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('mapCategoryRelation.edit.headline')}</h1>
      <div>
        <Link to="/mapCategoryRelations" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('mapCategoryRelation.edit.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(updateMapCategoryRelation)} noValidate>
      <InputRow useFormResult={useFormResult} object="mapCategoryRelation" field="id" disabled={true} type="number" />
      <InputRow useFormResult={useFormResult} object="mapCategoryRelation" field="createdAt" />
      <InputRow useFormResult={useFormResult} object="mapCategoryRelation" field="map" type="select" options={mapValues} />
      <input type="submit" value={t('mapCategoryRelation.edit.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
