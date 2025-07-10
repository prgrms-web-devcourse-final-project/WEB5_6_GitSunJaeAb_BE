import React from 'react';
import { createBrowserRouter, RouterProvider } from 'react-router';
import App from "./app";
import Home from './home/home';
import MemberList from './member/member-list';
import MemberAdd from './member/member-add';
import MemberEdit from './member/member-edit';
import MapList from './map/map-list';
import MapAdd from './map/map-add';
import MapEdit from './map/map-edit';
import MapEditorList from './map-editor/map-editor-list';
import MapEditorAdd from './map-editor/map-editor-add';
import MapEditorEdit from './map-editor/map-editor-edit';
import LayerList from './layer/layer-list';
import LayerAdd from './layer/layer-add';
import LayerEdit from './layer/layer-edit';
import MarkerList from './marker/marker-list';
import MarkerAdd from './marker/marker-add';
import MarkerEdit from './marker/marker-edit';
import CommentList from './comment/comment-list';
import CommentAdd from './comment/comment-add';
import CommentEdit from './comment/comment-edit';
import BookmarkList from './bookmark/bookmark-list';
import BookmarkAdd from './bookmark/bookmark-add';
import BookmarkEdit from './bookmark/bookmark-edit';
import MemberInterestList from './member-interest/member-interest-list';
import MemberInterestAdd from './member-interest/member-interest-add';
import MemberInterestEdit from './member-interest/member-interest-edit';
import CategoryList from './category/category-list';
import CategoryAdd from './category/category-add';
import CategoryEdit from './category/category-edit';
import MapCategoryRelationList from './map-category-relation/map-category-relation-list';
import MapCategoryRelationAdd from './map-category-relation/map-category-relation-add';
import MapCategoryRelationEdit from './map-category-relation/map-category-relation-edit';
import MapHashtagRelationList from './map-hashtag-relation/map-hashtag-relation-list';
import MapHashtagRelationAdd from './map-hashtag-relation/map-hashtag-relation-add';
import MapHashtagRelationEdit from './map-hashtag-relation/map-hashtag-relation-edit';
import HashtagList from './hashtag/hashtag-list';
import HashtagAdd from './hashtag/hashtag-add';
import HashtagEdit from './hashtag/hashtag-edit';
import ReportList from './report/report-list';
import ReportAdd from './report/report-add';
import ReportEdit from './report/report-edit';
import QuestList from './quest/quest-list';
import QuestAdd from './quest/quest-add';
import QuestEdit from './quest/quest-edit';
import MemberQuestList from './member-quest/member-quest-list';
import MemberQuestAdd from './member-quest/member-quest-add';
import MemberQuestEdit from './member-quest/member-quest-edit';
import MemberQuestEvidenceList from './member-quest-evidence/member-quest-evidence-list';
import MemberQuestEvidenceAdd from './member-quest-evidence/member-quest-evidence-add';
import MemberQuestEvidenceEdit from './member-quest-evidence/member-quest-evidence-edit';
import QuestRankList from './quest-rank/quest-rank-list';
import QuestRankAdd from './quest-rank/quest-rank-add';
import QuestRankEdit from './quest-rank/quest-rank-edit';
import LayerLibraryList from './layer-library/layer-library-list';
import LayerLibraryAdd from './layer-library/layer-library-add';
import LayerLibraryEdit from './layer-library/layer-library-edit';
import Error from './error/error';


export default function AppRoutes() {
  const router = createBrowserRouter([
    {
      element: <App />,
      children: [
        { path: '', element: <Home /> },
        { path: 'members', element: <MemberList /> },
        { path: 'members/add', element: <MemberAdd /> },
        { path: 'members/edit/:id', element: <MemberEdit /> },
        { path: 'maps', element: <MapList /> },
        { path: 'maps/add', element: <MapAdd /> },
        { path: 'maps/edit/:id', element: <MapEdit /> },
        { path: 'mapEditors', element: <MapEditorList /> },
        { path: 'mapEditors/add', element: <MapEditorAdd /> },
        { path: 'mapEditors/edit/:id', element: <MapEditorEdit /> },
        { path: 'layers', element: <LayerList /> },
        { path: 'layers/add', element: <LayerAdd /> },
        { path: 'layers/edit/:id', element: <LayerEdit /> },
        { path: 'markers', element: <MarkerList /> },
        { path: 'markers/add', element: <MarkerAdd /> },
        { path: 'markers/edit/:id', element: <MarkerEdit /> },
        { path: 'comments', element: <CommentList /> },
        { path: 'comments/add', element: <CommentAdd /> },
        { path: 'comments/edit/:id', element: <CommentEdit /> },
        { path: 'bookmarks', element: <BookmarkList /> },
        { path: 'bookmarks/add', element: <BookmarkAdd /> },
        { path: 'bookmarks/edit/:id', element: <BookmarkEdit /> },
        { path: 'memberInterests', element: <MemberInterestList /> },
        { path: 'memberInterests/add', element: <MemberInterestAdd /> },
        { path: 'memberInterests/edit/:id', element: <MemberInterestEdit /> },
        { path: 'categories', element: <CategoryList /> },
        { path: 'categories/add', element: <CategoryAdd /> },
        { path: 'categories/edit/:id', element: <CategoryEdit /> },
        { path: 'mapCategoryRelations', element: <MapCategoryRelationList /> },
        { path: 'mapCategoryRelations/add', element: <MapCategoryRelationAdd /> },
        { path: 'mapCategoryRelations/edit/:id', element: <MapCategoryRelationEdit /> },
        { path: 'mapHashtagRelations', element: <MapHashtagRelationList /> },
        { path: 'mapHashtagRelations/add', element: <MapHashtagRelationAdd /> },
        { path: 'mapHashtagRelations/edit/:id', element: <MapHashtagRelationEdit /> },
        { path: 'hashtags', element: <HashtagList /> },
        { path: 'hashtags/add', element: <HashtagAdd /> },
        { path: 'hashtags/edit/:id', element: <HashtagEdit /> },
        { path: 'reports', element: <ReportList /> },
        { path: 'reports/add', element: <ReportAdd /> },
        { path: 'reports/edit/:id', element: <ReportEdit /> },
        { path: 'quests', element: <QuestList /> },
        { path: 'quests/add', element: <QuestAdd /> },
        { path: 'quests/edit/:id', element: <QuestEdit /> },
        { path: 'memberQuests', element: <MemberQuestList /> },
        { path: 'memberQuests/add', element: <MemberQuestAdd /> },
        { path: 'memberQuests/edit/:id', element: <MemberQuestEdit /> },
        { path: 'memberQuestEvidences', element: <MemberQuestEvidenceList /> },
        { path: 'memberQuestEvidences/add', element: <MemberQuestEvidenceAdd /> },
        { path: 'memberQuestEvidences/edit/:id', element: <MemberQuestEvidenceEdit /> },
        { path: 'questRanks', element: <QuestRankList /> },
        { path: 'questRanks/add', element: <QuestRankAdd /> },
        { path: 'questRanks/edit/:id', element: <QuestRankEdit /> },
        { path: 'layerLibraries', element: <LayerLibraryList /> },
        { path: 'layerLibraries/add', element: <LayerLibraryAdd /> },
        { path: 'layerLibraries/edit/:id', element: <LayerLibraryEdit /> },
        { path: 'error', element: <Error /> },
        { path: '*', element: <Error /> }
      ]
    }
  ]);

  return (
    <RouterProvider router={router} />
  );
}
