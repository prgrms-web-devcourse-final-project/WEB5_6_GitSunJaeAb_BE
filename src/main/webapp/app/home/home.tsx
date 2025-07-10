import React from 'react';
import { Link } from 'react-router';
import { Trans, useTranslation } from 'react-i18next';
import useDocumentTitle from 'app/common/use-document-title';
import './home.css';


export default function Home() {
  const { t } = useTranslation();
  useDocumentTitle(t('home.index.headline'));

  return (<>
    <h1 className="grow text-3xl md:text-4xl font-medium mb-8">{t('home.index.headline')}</h1>
    <p className="mb-4"><Trans i18nKey="home.index.text" components={{ a: <a />, strong: <strong /> }} /></p>
    <p className="mb-12">
      <span>{t('home.index.swagger.text')}</span>
      <span> </span>
      <a href={process.env.API_PATH + '/swagger-ui.html'} target="_blank" className="underline">{t('home.index.swagger.link')}</a>.
    </p>
    <div className="md:w-2/5 mb-12">
      <h4 className="text-2xl font-medium mb-4">{t('home.index.exploreEntities')}</h4>
      <div className="flex flex-col border border-gray-300 rounded">
        <Link to="/members" className="w-full border-gray-300 hover:bg-gray-100 border-b rounded-t px-4 py-2">{t('member.list.headline')}</Link>
        <Link to="/maps" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('map.list.headline')}</Link>
        <Link to="/mapEditors" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('mapEditor.list.headline')}</Link>
        <Link to="/layers" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('layer.list.headline')}</Link>
        <Link to="/markers" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('marker.list.headline')}</Link>
        <Link to="/comments" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('comment.list.headline')}</Link>
        <Link to="/bookmarks" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('bookmark.list.headline')}</Link>
        <Link to="/memberInterests" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('memberInterest.list.headline')}</Link>
        <Link to="/categories" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('category.list.headline')}</Link>
        <Link to="/mapCategoryRelations" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('mapCategoryRelation.list.headline')}</Link>
        <Link to="/mapHashtagRelations" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('mapHashtagRelation.list.headline')}</Link>
        <Link to="/hashtags" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('hashtag.list.headline')}</Link>
        <Link to="/reports" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('report.list.headline')}</Link>
        <Link to="/quests" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('quest.list.headline')}</Link>
        <Link to="/memberQuests" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('memberQuest.list.headline')}</Link>
        <Link to="/memberQuestEvidences" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('memberQuestEvidence.list.headline')}</Link>
        <Link to="/questRanks" className="w-full border-gray-300 hover:bg-gray-100 border-b px-4 py-2">{t('questRank.list.headline')}</Link>
        <Link to="/layerLibraries" className="w-full border-gray-300 hover:bg-gray-100 rounded-b px-4 py-2">{t('layerLibrary.list.headline')}</Link>
      </div>
    </div>
  </>);
}
