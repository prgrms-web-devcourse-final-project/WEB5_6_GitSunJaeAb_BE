import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate, useParams } from 'react-router';
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

export default function CommentEdit() {
  const { t } = useTranslation();
  useDocumentTitle(t('comment.edit.headline'));

  const navigate = useNavigate();
  const [roadmapValues, setRoadmapValues] = useState<Map<number,string>>(new Map());
  const [memberValues, setMemberValues] = useState<Map<number,string>>(new Map());
  const params = useParams();
  const currentId = +params.id!;

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareForm = async () => {
    try {
      const mapValuesResponse = await axios.get('/comments/roadmapValues');
      setRoadmapValues(mapValuesResponse.data);
      const memberValuesResponse = await axios.get('/comments/memberValues');
      setMemberValues(memberValuesResponse.data);
      const data = (await axios.get('/comments/' + currentId)).data;
      useFormResult.reset(data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareForm();
  }, []);

  const updateComment = async (data: CommentDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.put('/comments/' + currentId, data);
      navigate('/comments', {
            state: {
              msgSuccess: t('comment.update.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('comment.edit.headline')}</h1>
      <div>
        <Link to="/comments" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('comment.edit.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(updateComment)} noValidate>
      <InputRow useFormResult={useFormResult} object="comment" field="id" disabled={true} type="number" />
      <InputRow useFormResult={useFormResult} object="comment" field="content" required={true} type="textarea" />
      <InputRow useFormResult={useFormResult} object="comment" field="createdAt" required={true} />
      <InputRow useFormResult={useFormResult} object="comment" field="updatedAt" />
      <InputRow useFormResult={useFormResult} object="comment" field="roadmap" type="select" options={roadmapValues} />
      <InputRow useFormResult={useFormResult} object="comment" field="member" type="select" options={memberValues} />
      <input type="submit" value={t('comment.edit.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
