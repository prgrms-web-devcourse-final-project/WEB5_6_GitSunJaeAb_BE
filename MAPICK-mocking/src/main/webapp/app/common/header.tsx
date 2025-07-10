import React, { useEffect, useRef } from 'react';
import { Link } from 'react-router';
import { useTranslation } from 'react-i18next';


export default function Header() {
  const { t } = useTranslation();
  const headerRef = useRef<HTMLElement|null>(null);

  const handleClick = (event: Event) => {
    // close any open dropdown
    const $clickedDropdown = (event.target as HTMLElement).closest('.js-dropdown');
    const $dropdowns = headerRef.current!.querySelectorAll('.js-dropdown');
    $dropdowns.forEach(($dropdown:Element) => {
      if ($clickedDropdown !== $dropdown && $dropdown.getAttribute('data-dropdown-keepopen') !== 'true') {
        $dropdown.ariaExpanded = 'false';
        $dropdown.nextElementSibling!.classList.add('hidden');
      }
    });
    // toggle selected if applicable
    if ($clickedDropdown) {
      $clickedDropdown.ariaExpanded = '' + ($clickedDropdown.ariaExpanded !== 'true');
      $clickedDropdown.nextElementSibling!.classList.toggle('hidden');
    }
  };

  useEffect(() => {
    document.body.addEventListener('click', handleClick);
    return () => document.body.removeEventListener('click', handleClick);
  }, []);

  return (
    <header ref={headerRef} className="bg-gray-50">
      <div className="container mx-auto px-4 md:px-6">
        <nav className="flex flex-wrap items-center justify-between py-2">
          <Link to="/" className="flex py-1.5 mr-4">
            <img src="/images/logo.png" alt={t('app.title')} width="30" height="30" className="inline-block" />
            <span className="text-xl pl-3">{t('app.title')}</span>
          </Link>
          <button type="button" className="js-dropdown md:hidden border rounded cursor-pointer" data-dropdown-keepopen="true"
              aria-label={t('navigation.toggle')} aria-controls="navbarToggle" aria-expanded="false">
            <div className="space-y-1.5 my-2.5 mx-4">
              <div className="w-6 h-0.5 bg-gray-500"></div>
              <div className="w-6 h-0.5 bg-gray-500"></div>
              <div className="w-6 h-0.5 bg-gray-500"></div>
            </div>
          </button>
          <div className="hidden md:block flex grow md:grow-0 justify-end basis-full md:basis-auto pt-3 md:pt-1 pb-1" id="navbarToggle">
            <ul className="flex">
              <li>
                <Link to="/" className="block text-gray-500 p-2">{t('navigation.home')}</Link>
              </li>
              <li className="relative">
                <button type="button" className="js-dropdown block text-gray-500 p-2 cursor-pointer" id="navbarEntitiesLink"
                    aria-expanded="false">
                  <span>{t('navigation.entities')}</span>
                  <span className="text-[9px] align-[3px] pl-0.5">&#9660;</span>
                </button>
                <ul className="hidden block absolute right-0 bg-white border border-gray-300 rounded min-w-[10rem] py-2" aria-labelledby="navbarEntitiesLink">
                  <li><Link to="/members" className="inline-block w-full hover:bg-gray-200 px-4 py-1">{t('member.list.headline')}</Link></li>
                  <li><Link to="/maps" className="inline-block w-full hover:bg-gray-200 px-4 py-1">{t('map.list.headline')}</Link></li>
                  <li><Link to="/roadmapEditors" className="inline-block w-full hover:bg-gray-200 px-4 py-1">{t('mapEditor.list.headline')}</Link></li>
                  <li><Link to="/layers" className="inline-block w-full hover:bg-gray-200 px-4 py-1">{t('layer.list.headline')}</Link></li>
                  <li><Link to="/markers" className="inline-block w-full hover:bg-gray-200 px-4 py-1">{t('marker.list.headline')}</Link></li>
                  <li><Link to="/comments" className="inline-block w-full hover:bg-gray-200 px-4 py-1">{t('comment.list.headline')}</Link></li>
                  <li><Link to="/bookmarks" className="inline-block w-full hover:bg-gray-200 px-4 py-1">{t('bookmark.list.headline')}</Link></li>
                  <li><Link to="/memberInterests" className="inline-block w-full hover:bg-gray-200 px-4 py-1">{t('memberInterest.list.headline')}</Link></li>
                  <li><Link to="/categories" className="inline-block w-full hover:bg-gray-200 px-4 py-1">{t('category.list.headline')}</Link></li>
                  <li><Link to="/roadmapCategoryRelations" className="inline-block w-full hover:bg-gray-200 px-4 py-1">{t('mapCategoryRelation.list.headline')}</Link></li>
                  <li><Link to="/roadmapHashtagRelations" className="inline-block w-full hover:bg-gray-200 px-4 py-1">{t('mapHashtagRelation.list.headline')}</Link></li>
                  <li><Link to="/hashtags" className="inline-block w-full hover:bg-gray-200 px-4 py-1">{t('hashtag.list.headline')}</Link></li>
                  <li><Link to="/reports" className="inline-block w-full hover:bg-gray-200 px-4 py-1">{t('report.list.headline')}</Link></li>
                  <li><Link to="/quests" className="inline-block w-full hover:bg-gray-200 px-4 py-1">{t('quest.list.headline')}</Link></li>
                  <li><Link to="/memberQuests" className="inline-block w-full hover:bg-gray-200 px-4 py-1">{t('memberQuest.list.headline')}</Link></li>
                  <li><Link to="/memberQuestEvidences" className="inline-block w-full hover:bg-gray-200 px-4 py-1">{t('memberQuestEvidence.list.headline')}</Link></li>
                  <li><Link to="/questRanks" className="inline-block w-full hover:bg-gray-200 px-4 py-1">{t('questRank.list.headline')}</Link></li>
                  <li><Link to="/layerLibraries" className="inline-block w-full hover:bg-gray-200 px-4 py-1">{t('layerLibrary.list.headline')}</Link></li>
                </ul>
              </li>
            </ul>
          </div>
        </nav>
      </div>
    </header>
  );
}
