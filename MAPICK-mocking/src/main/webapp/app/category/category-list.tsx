import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError } from 'app/common/utils';
// import { CategoryDTO } from 'app/category/category-model';
import axios from 'axios';
import useDocumentTitle from 'app/common/use-document-title';


export default function CategoryList() {
  const { t } = useTranslation();
  useDocumentTitle(t('category.list.headline'));

  type CategoryRequest = {
    id: number;
    name: string;
    categoryImage?: string;
    createdAt: string;
    roadmapCategoryRelations?: string;
  };

  const [categories, setCategories] = useState<CategoryRequest[]>([]);
  const navigate = useNavigate();

  const getAllCategories = async () => {
    try {
      const response = await axios.get('/categories');
      setCategories(response.data.categories);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/categories/' + id);
      navigate('/categories', {
            state: {
              msgInfo: t('category.delete.success')
            }
          });
      getAllCategories();
    } catch (error: any) {
      if (error?.response?.data?.code === 'REFERENCED') {
        const messageParts = error.response.data.message.split(',');
        navigate('/categories', {
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
    getAllCategories();
  }, []);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('category.list.headline')}</h1>
      <div>
        <Link to="/categories/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('category.list.createNew')}</Link>
      </div>
    </div>
    {!categories || categories.length === 0 ? (
    <div>{t('category.list.empty')}</div>
    ) : (
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('category.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('category.name.label')}</th>
            <th scope="col" className="text-left p-2">{t('category.categoryImage.label')}</th>
            <th scope="col" className="text-left p-2">{t('category.createdAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('category.roadmapCategoryRelations.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {categories.map((category) => (
          <tr key={category.id} className="odd:bg-gray-100">
            <td className="p-2">{category.id}</td>
            <td className="p-2">{category.name}</td>
            <td className="p-2">{category.categoryImage}</td>
            <td className="p-2">{category.createdAt}</td>
            <td className="p-2">{category.roadmapCategoryRelations}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/categories/edit/' + category.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('category.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(category.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('category.list.delete')}</button>
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
