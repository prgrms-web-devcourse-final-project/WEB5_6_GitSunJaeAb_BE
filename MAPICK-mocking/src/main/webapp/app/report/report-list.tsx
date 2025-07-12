import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError } from 'app/common/utils';
import { ReportDTO } from 'app/report/report-model';
import axios from 'axios';
import useDocumentTitle from 'app/common/use-document-title';


export default function ReportList() {
  const { t } = useTranslation();
  useDocumentTitle(t('report.list.headline'));

  const [reports, setReports] = useState<ReportDTO[]>([]);
  const navigate = useNavigate();

  const getAllReports = async () => {
    try {
      const response = await axios.get('admin/reports');
      setReports(response.data.reports);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  const confirmDelete = async (id: number) => {
    if (!confirm(t('delete.confirm'))) {
      return;
    }
    try {
      await axios.delete('/admin/reports' + id);
      navigate('/reports', {
            state: {
              msgInfo: t('report.delete.success')
            }
          });
      getAllReports();
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    getAllReports();
  }, []);

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('report.list.headline')}</h1>
      <div>
        <Link to="/reports/add" className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2">{t('report.list.createNew')}</Link>
      </div>
    </div>
    {!reports || reports.length === 0 ? (
    <div>{t('report.list.empty')}</div>
    ) : (
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr>
            <th scope="col" className="text-left p-2">{t('report.id.label')}</th>
            <th scope="col" className="text-left p-2">{t('report.status.label')}</th>
            <th scope="col" className="text-left p-2">{t('report.createdAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('report.resolvedAt.label')}</th>
            <th scope="col" className="text-left p-2">{t('report.reporter.label')}</th>
            <th scope="col" className="text-left p-2">{t('report.reportedMember.label')}</th>
            <th scope="col" className="text-left p-2">{t('report.roadmap.label')}</th>
            <th scope="col" className="text-left p-2">{t('report.marker.label')}</th>
            <th scope="col" className="text-left p-2">{t('report.quest.label')}</th>
            <th></th>
          </tr>
        </thead>
        <tbody className="border-t-2 border-black">
          {reports.map((report) => (
          <tr key={report.id} className="odd:bg-gray-100">
            <td className="p-2">{report.id}</td>
            <td className="p-2">{report.status}</td>
            <td className="p-2">{report.createdAt}</td>
            <td className="p-2">{report.resolvedAt}</td>
            <td className="p-2">{report.reporter}</td>
            <td className="p-2">{report.reportedMember}</td>
            <td className="p-2">{report.roadmap}</td>
            <td className="p-2">{report.marker}</td>
            <td className="p-2">{report.quest}</td>
            <td className="p-2">
              <div className="float-right whitespace-nowrap">
                <Link to={'/reports/edit/' + report.id} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('report.list.edit')}</Link>
                <span> </span>
                <button type="button" onClick={() => confirmDelete(report.id!)} className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-3 rounded px-2.5 py-1.5 text-sm">{t('report.list.delete')}</button>
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
