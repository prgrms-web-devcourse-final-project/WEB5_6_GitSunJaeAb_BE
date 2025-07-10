import React from 'react';
import { createBrowserRouter, RouterProvider } from 'react-router';
import App from "./app";
import Home from './home/home';
import MemberList from './member/member-list';
import MemberAdd from './member/member-add';
import MemberEdit from './member/member-edit';
import RoadmapList from './roadmap/roadmap-list';
import RoadmapAdd from './roadmap/roadmap-add';
import RoadmapEdit from './roadmap/roadmap-edit';
import RoadmapEditorList from './roadmap-editor/roadmap-editor-list';
import RoadmapEditorAdd from './roadmap-editor/roadmap-editor-add';
import RoadmapEditorEdit from './roadmap-editor/roadmap-editor-edit';
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
import RoadmapCategoryRelationList from './roadmap-category-relation/roadmap-category-relation-list';
import RoadmapCategoryRelationAdd from './roadmap-category-relation/roadmap-category-relation-add';
import RoadmapCategoryRelationEdit from './roadmap-category-relation/roadmap-category-relation-edit';
import RoadmapHashtagRelationList from './roadmap-hashtag-relation/roadmap-hashtag-relation-list';
import RoadmapHashtagRelationAdd from './roadmap-hashtag-relation/roadmap-hashtag-relation-add';
import RoadmapHashtagRelationEdit from './roadmap-hashtag-relation/roadmap-hashtag-relation-edit';
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
        { path: 'maps', element: <RoadmapList /> },
        { path: 'maps/add', element: <RoadmapAdd /> },
        { path: 'maps/edit/:id', element: <RoadmapEdit /> },
        { path: 'mapEditors', element: <RoadmapEditorList /> },
        { path: 'mapEditors/add', element: <RoadmapEditorAdd /> },
        { path: 'mapEditors/edit/:id', element: <RoadmapEditorEdit /> },
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
        { path: 'mapCategoryRelations', element: <RoadmapCategoryRelationList /> },
        { path: 'mapCategoryRelations/add', element: <RoadmapCategoryRelationAdd /> },
        { path: 'mapCategoryRelations/edit/:id', element: <RoadmapCategoryRelationEdit /> },
        { path: 'mapHashtagRelations', element: <RoadmapHashtagRelationList /> },
        { path: 'mapHashtagRelations/add', element: <RoadmapHashtagRelationAdd /> },
        { path: 'mapHashtagRelations/edit/:id', element: <RoadmapHashtagRelationEdit /> },
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
