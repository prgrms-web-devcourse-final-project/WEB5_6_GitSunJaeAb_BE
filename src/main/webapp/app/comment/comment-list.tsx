import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError } from 'app/common/utils';
import { CommentDTO } from 'app/comment/comment-model';
import axios from 'axios';
import useDocumentTitle from 'app/common/use-document-title';


export default function CommentList() {
  const { t } = useTranslation();
  useDocumentTitle(t('comment.list.headline'));

  const [comments, setComments] = useState<CommentDTO[]>([]);
  const navigate = useNavigate();

  const getAllComments = async () => {
    try {
      const response = await axios.get('/api/comments');
      setComments(response.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/api/comments/' + id);
      navigate('/comments', {
            state: {
              msgInfo: t('comment.delete.success')
            }
          });
      getAllComments();
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    getAllComments();
  }, []);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('comment.list.headline')}</h1>
      <div>
        <Link to="/comments/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('comment.list.createNew')}</Link>
      </div>
    </div>
    {!comments || comments.length === 0 ? (
    <div>{t('comment.list.empty')}</div>
    ) : (
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('comment.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('comment.createdAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('comment.updatedAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('comment.map.label')}</th>
            <th scope="col" className="text-left p-2">{t('comment.member.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {comments.map((comment) => (
          <tr key={comment.id} className="odd:bg-gray-100">
            <td className="p-2">{comment.id}</td>
            <td className="p-2">{comment.createdAt}</td>
            <td className="p-2">{comment.updatedAt}</td>
            <td className="p-2">{comment.map}</td>
            <td className="p-2">{comment.member}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/comments/edit/' + comment.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('comment.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(comment.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('comment.list.delete')}</button>
              </div>
            </td>
          </tr>
          ))}
        </tbody>
      </table>
    </div>
    )}
  </>);
}
