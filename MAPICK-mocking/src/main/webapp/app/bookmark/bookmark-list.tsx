import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError } from 'app/common/utils';
import { BookmarkDTO } from 'app/bookmark/bookmark-model';
import axios from 'axios';
import useDocumentTitle from 'app/common/use-document-title';


export default function BookmarkList() {
  const { t } = useTranslation();
  useDocumentTitle(t('bookmark.list.headline'));

  const [bookmarks, setBookmarks] = useState<BookmarkDTO[]>([]);
  const navigate = useNavigate();

  const getAllBookmarks = async () => {
    try {
      const response = await axios.get('/bookmarks');
      setBookmarks(response.data.content);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/bookmarks/' + id);
      navigate('/bookmarks', {
            state: {
              msgInfo: t('bookmark.delete.success')
            }
          });
      getAllBookmarks();
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    getAllBookmarks();
  }, []);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('bookmark.list.headline')}</h1>
      <div>
        <Link to="/bookmarks/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('bookmark.list.createNew')}</Link>
      </div>
    </div>
    {!bookmarks || bookmarks.length === 0 ? (
    <div>{t('bookmark.list.empty')}</div>
    ) : (
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('bookmark.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('bookmark.createdAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('bookmark.roadmap.label')}</th>
            <th scope="col" className="text-left p-2">{t('bookmark.member.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {bookmarks.map((bookmark) => (
          <tr key={bookmark.id} className="odd:bg-gray-100">
            <td className="p-2">{bookmark.id}</td>
            <td className="p-2">{bookmark.createdAt}</td>
            <td className="p-2">{bookmark.roadmap}</td>
            <td className="p-2">{bookmark.member}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/bookmarks/edit/' + bookmark.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('bookmark.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(bookmark.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('bookmark.list.delete')}</button>
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
