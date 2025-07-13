import React, {useEffect} from 'react';
import {useTranslation} from 'react-i18next';
import {Link, useNavigate, useParams} from 'react-router';
import {handleServerError, setYupDefaults} from 'app/common/utils';
import {Resolver, useForm} from 'react-hook-form';
import {yupResolver} from '@hookform/resolvers/yup';
// import { CategoryDTO } from 'app/category/category-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';

interface CategoryFormData {
  name: string;
  description?: string | null;
  categoryImage?: FileList | null;
}


function getSchema() {
  setYupDefaults();
  return yup.object({
    name: yup.string().emptyToNull().max(255).required(),
    description: yup.string().max(255).nullable(),
    categoryImage: yup
    .mixed()
    .test("fileType", "지원하지 않는 파일 형식입니다", (value) => {
      return value == null || value instanceof FileList;
    })
  });
}

export default function CategoryEdit() {
  const {t} = useTranslation();
  useDocumentTitle(t('category.edit.headline'));

  const navigate = useNavigate();
  const params = useParams();
  const currentId = +params.id!;

  const useFormResult = useForm<CategoryFormData>({
    resolver: yupResolver(getSchema()) as Resolver<CategoryFormData>,
  });

  const prepareForm = async () => {
    try {
      const data = (await axios.get('/categories/' + currentId)).data;
      useFormResult.reset(data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareForm();
  }, []);


  const updateCategory = async (formData: FormData) => {
    window.scrollTo(0, 0);
    try {
      await axios.put('/categories/' + currentId, formData, {
        headers: {'Content-Type': 'multipart/form-data'}
      });
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
        <Link to="/categories"
              className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('category.edit.back')}</Link>
      </div>
    </div>

    <form
        onSubmit={useFormResult.handleSubmit((formData) => {
          // PUT 요청에서 필요한 필드만 전송 (name이 필수)
          const updateData = new FormData();

          // 필수 필드: name
          if (formData.name) {
            updateData.append("name", formData.name);
          }

          // 선택적 필드: description
          if (formData.description !== undefined && formData.description !== null) {
            updateData.append("description", formData.description);
          }

          // 파일 업로드 처리
          if (formData.categoryImage && formData.categoryImage[0]) {
            updateData.append("imageFile", formData.categoryImage[0]);
          }

          return updateCategory(updateData);
        })}
        noValidate encType="multipart/form-data">
      <InputRow useFormResult={useFormResult} object="category" field="id" disabled={true}
                type="number"/>
      <InputRow useFormResult={useFormResult} object="category" field="name" required={true}/>
      <InputRow useFormResult={useFormResult} object="category" field="description"
                type="textarea"/>
      <InputRow useFormResult={useFormResult} object="category" field="categoryImage" type="file"/>
      {/* createdAt 필드는 수정 시 제외 */}
      <input type="submit" value={t('category.edit.headline')}
             className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6"/>
    </form>
  </>);
}
