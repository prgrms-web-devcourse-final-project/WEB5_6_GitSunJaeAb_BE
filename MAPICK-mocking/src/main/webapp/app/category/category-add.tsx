import React from 'react';
import {useTranslation} from 'react-i18next';
import {Link, useNavigate} from 'react-router';
import {handleServerError, setYupDefaults} from 'app/common/utils';
import {useForm} from 'react-hook-form';
import {yupResolver} from '@hookform/resolvers/yup';
// import { CategoryDTO } from 'app/category/category-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    name: yup.string().max(255).required(),
    description: yup.string().emptyToNull(),
    categoryImage: yup.string().emptyToNull().max(255)
  });
}

export default function CategoryAdd() {
  const {t} = useTranslation();
  useDocumentTitle(t('category.add.headline'));

  const navigate = useNavigate();

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  type CategoryRequest = {
    name: string;
    description?: string | null;
    categoryImage?: string | null;
  };

  const createCategory = async (data: CategoryRequest) => {
    window.scrollTo(0, 0);
    try {
      // FormData 객체 생성
      const formData = new FormData();
      formData.append('name', data.name);
      if (data.description) formData.append('description', data.description);

      // 파일 input에서 파일 가져오기
      const fileInput = document.getElementById('imageFile') as HTMLInputElement;
      if (fileInput && fileInput.files && fileInput.files[0]) {
        formData.append('imageFile', fileInput.files[0]);
      }

      // multipart/form-data로 전송
      await axios.post('/categories', formData, {
        headers: {'Content-Type': 'multipart/form-data'}
      });

      navigate('/categories', {
        state: {msgSuccess: t('category.create.success')}
      });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('category.add.headline')}</h1>
      <div>
        <Link to="/categories"
              className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('category.add.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(createCategory)} noValidate
          encType="multipart/form-data">
      <InputRow useFormResult={useFormResult} object="category" field="name" required={true}/>
      <InputRow useFormResult={useFormResult} object="category" field="description"
                type="textarea"/>
      <div className="mb-4">
        <label htmlFor="imageFile" className="block mb-1 text-sm font-medium text-gray-700">카테고리
          이미지</label>
        <input id="imageFile" name="imageFile" type="file" accept="image/*"
               className="block w-full text-sm text-gray-900 border border-gray-300 rounded-lg cursor-pointer"/>
      </div>
      <input type="submit" value={t('category.add.headline')}
             className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6"/>
    </form>
  </>);
}
