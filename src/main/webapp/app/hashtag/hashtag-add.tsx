import React from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { HashtagDTO } from 'app/hashtag/hashtag-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    name: yup.string().emptyToNull().max(255).required(),
    createdAt: yup.string().emptyToNull().offsetDateTime()
  });
}

export default function HashtagAdd() {
  const { t } = useTranslation();
  useDocumentTitle(t('hashtag.add.headline'));

  const navigate = useNavigate();

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const createHashtag = async (data: HashtagDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.post('/api/hashtags', data);
      navigate('/hashtags', {
            state: {
              msgSuccess: t('hashtag.create.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('hashtag.add.headline')}</h1>
      <div>
        <Link to="/hashtags" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('hashtag.add.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(createHashtag)} noValidate>
      <InputRow useFormResult={useFormResult} object="hashtag" field="name" required={true} />
      <InputRow useFormResult={useFormResult} object="hashtag" field="createdAt" />
      <input type="submit" value={t('hashtag.add.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
