import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useParams } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { CategoryDTO } from 'app/category/category-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    name: yup.string().emptyToNull().max(255).required(),
    description: yup.string().emptyToNull(),
    categoryImage: yup.string().emptyToNull().max(255),
    createdAt: yup.string().emptyToNull().offsetDateTime().required(),
    mapCategoryRelations: yup.number().integer().emptyToNull()
  });
}

export default function CategoryEdit() {
  const { t } = useTranslation();
  useDocumentTitle(t('category.edit.headline'));

  const navigate = useNavigate();
  const [mapCategoryRelationsValues, setMapCategoryRelationsValues] = useState<Map<number,string>>(new Map());
  const params = useParams();
  const currentId = +params.id!;

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareForm = async () => {
    try {
      const mapCategoryRelationsValuesResponse = await axios.get('/api/categories/mapCategoryRelationsValues');
      setMapCategoryRelationsValues(mapCategoryRelationsValuesResponse.data);
      const data = (await axios.get('/api/categories/' + currentId)).data;
      useFormResult.reset(data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareForm();
  }, []);

  const updateCategory = async (data: CategoryDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.put('/api/categories/' + currentId, data);
      navigate('/categories', {
            state: {
              msgSuccess: t('category.update.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('category.edit.headline')}</h1>
      <div>
        <Link to="/categories" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('category.edit.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(updateCategory)} noValidate>
      <InputRow useFormResult={useFormResult} object="category" field="id" disabled={true} type="number" />
      <InputRow useFormResult={useFormResult} object="category" field="name" required={true} />
      <InputRow useFormResult={useFormResult} object="category" field="description" type="textarea" />
      <InputRow useFormResult={useFormResult} object="category" field="categoryImage" />
      <InputRow useFormResult={useFormResult} object="category" field="createdAt" required={true} />
      <InputRow useFormResult={useFormResult} object="category" field="mapCategoryRelations" type="select" options={mapCategoryRelationsValues} />
      <input type="submit" value={t('category.edit.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
