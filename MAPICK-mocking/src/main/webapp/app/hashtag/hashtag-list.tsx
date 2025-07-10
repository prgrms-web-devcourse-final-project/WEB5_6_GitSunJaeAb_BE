import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError } from 'app/common/utils';
import { HashtagDTO } from 'app/hashtag/hashtag-model';
import axios from 'axios';
import useDocumentTitle from 'app/common/use-document-title';


export default function HashtagList() {
  const { t } = useTranslation();
  useDocumentTitle(t('hashtag.list.headline'));

  const [hashtags, setHashtags] = useState<HashtagDTO[]>([]);
  const navigate = useNavigate();

  const getAllHashtags = async () => {
    try {
      const response = await axios.get('/hashtags');
      setHashtags(response.data.content);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/hashtags/' + id);
      navigate('/hashtags', {
            state: {
              msgInfo: t('hashtag.delete.success')
            }
          });
      getAllHashtags();
    } catch (error: any) {
      if (error?.response?.data?.code === 'REFERENCED') {
        const messageParts = error.response.data.message.split(',');
        navigate('/hashtags', {
              state: {
                msgError: t(messageParts[0]!, { id: messageParts[1]! })
              }
            });
        return;
      }
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    getAllHashtags();
  }, []);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('hashtag.list.headline')}</h1>
      <div>
        <Link to="/hashtags/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('hashtag.list.createNew')}</Link>
      </div>
    </div>
    {!hashtags || hashtags.length === 0 ? (
    <div>{t('hashtag.list.empty')}</div>
    ) : (
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('hashtag.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('hashtag.name.label')}</th>
            <th scope="col" className="text-left p-2">{t('hashtag.createdAt.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {hashtags.map((hashtag) => (
          <tr key={hashtag.id} className="odd:bg-gray-100">
            <td className="p-2">{hashtag.id}</td>
            <td className="p-2">{hashtag.name}</td>
            <td className="p-2">{hashtag.createdAt}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/hashtags/edit/' + hashtag.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('hashtag.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(hashtag.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('hashtag.list.delete')}</button>
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
