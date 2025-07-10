import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { CommentDTO } from 'app/comment/comment-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    content: yup.string().emptyToNull().required(),
    createdAt: yup.string().emptyToNull().offsetDateTime().required(),
    updatedAt: yup.string().emptyToNull().offsetDateTime(),
    roadmap: yup.number().integer().emptyToNull(),
    member: yup.number().integer().emptyToNull()
  });
}

export default function CommentAdd() {
  const { t } = useTranslation();
  useDocumentTitle(t('comment.add.headline'));

  const navigate = useNavigate();
  const [mapValues, setMapValues] = useState<Map<number,string>>(new Map());
  const [memberValues, setMemberValues] = useState<Map<number,string>>(new Map());

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareRelations = async () => {
    try {
      const mapValuesResponse = await axios.get('/api/comments/mapValues');
      setMapValues(mapValuesResponse.data);
      const memberValuesResponse = await axios.get('/api/comments/memberValues');
      setMemberValues(memberValuesResponse.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareRelations();
  }, []);

  const createComment = async (data: CommentDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.post('/api/comments', data);
      navigate('/comments', {
            state: {
              msgSuccess: t('comment.create.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('comment.add.headline')}</h1>
      <div>
        <Link to="/comments" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('comment.add.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(createComment)} noValidate>
      <InputRow useFormResult={useFormResult} object="comment" field="content" required={true} type="textarea" />
      <InputRow useFormResult={useFormResult} object="comment" field="createdAt" required={true} />
      <InputRow useFormResult={useFormResult} object="comment" field="updatedAt" />
      <InputRow useFormResult={useFormResult} object="comment" field="roadmap" type="select" options={mapValues} />
      <InputRow useFormResult={useFormResult} object="comment" field="member" type="select" options={memberValues} />
      <input type="submit" value={t('comment.add.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
