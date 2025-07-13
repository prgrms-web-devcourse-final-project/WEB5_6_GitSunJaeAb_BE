import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router';
import { handleServerError, setYupDefaults } from 'app/common/utils';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { ReportDTO } from 'app/report/report-model';
import axios from 'axios';
import InputRow from 'app/common/input-row/input-row';
import useDocumentTitle from 'app/common/use-document-title';
import * as yup from 'yup';


function getSchema() {
  setYupDefaults();
  return yup.object({
    reportType: yup.string().emptyToNull().max(255),
    description: yup.string().emptyToNull(),
    status: yup.string().emptyToNull().max(255).required(),
    createdAt: yup.string().emptyToNull().offsetDateTime(),
    resolvedAt: yup.string().emptyToNull().offsetDateTime(),
    reporter: yup.number().integer().emptyToNull(),
    reportedMember: yup.number().integer().emptyToNull(),
    roadmap: yup.number().integer().emptyToNull(),
    marker: yup.number().integer().emptyToNull(),
    quest: yup.number().integer().emptyToNull()
  });
}

export default function ReportAdd() {
  const { t } = useTranslation();
  useDocumentTitle(t('report.add.headline'));

  const navigate = useNavigate();
  const [reporterValues, setReporterValues] = useState<Map<number,string>>(new Map());
  const [reportedMemberValues, setReportedMemberValues] = useState<Map<number,string>>(new Map());
  const [mapValues, setRoadmapValues] = useState<Map<number,string>>(new Map());
  const [markerValues, setMarkerValues] = useState<Map<number,string>>(new Map());
  const [questValues, setQuestValues] = useState<Map<number,string>>(new Map());

  const useFormResult = useForm({
    resolver: yupResolver(getSchema()),
  });

  const prepareRelations = async () => {
    try {
      const reporterValuesResponse = await axios.get('/reports/reporterValues');
      setReporterValues(reporterValuesResponse.data);
      const reportedMemberValuesResponse = await axios.get('/reports/reportedMemberValues');
      setReportedMemberValues(reportedMemberValuesResponse.data);
      const roadmapValuesResponse = await axios.get('/reports/roadmapValues');
      setRoadmapValues(roadmapValuesResponse.data);
      const markerValuesResponse = await axios.get('/reports/markerValues');
      setMarkerValues(markerValuesResponse.data);
      const questValuesResponse = await axios.get('/reports/questValues');
      setQuestValues(questValuesResponse.data);
    } catch (error: any) {
      handleServerError(error, navigate);
    }
  };

  useEffect(() => {
    prepareRelations();
  }, []);

  const createReport = async (data: ReportDTO) => {
    window.scrollTo(0, 0);
    try {
      await axios.post('/reports', data);
      navigate('/reports', {
            state: {
              msgSuccess: t('report.create.success')
            }
          });
    } catch (error: any) {
      handleServerError(error, navigate, useFormResult.setError, t);
    }
  };

  return (<>
    <div className="flex flex-wrap mb-6">
      <h1 className="grow text-3xl md:text-4xl font-medium mb-2">{t('report.add.headline')}</h1>
      <div>
        <Link to="/reports" className="inline-block text-white bg-gray-500 hover:bg-gray-600 focus:ring-gray-200 focus:ring-4 rounded px-5 py-2">{t('report.add.back')}</Link>
      </div>
    </div>
    <form onSubmit={useFormResult.handleSubmit(createReport)} noValidate>
      <InputRow useFormResult={useFormResult} object="report" field="reportType" />
      <InputRow useFormResult={useFormResult} object="report" field="description" type="textarea" />
      <InputRow useFormResult={useFormResult} object="report" field="status" required={true} />
      <InputRow useFormResult={useFormResult} object="report" field="createdAt" />
      <InputRow useFormResult={useFormResult} object="report" field="resolvedAt" />
      <InputRow useFormResult={useFormResult} object="report" field="reporter" type="select" options={reporterValues} />
      <InputRow useFormResult={useFormResult} object="report" field="reportedMember" type="select" options={reportedMemberValues} />
      <InputRow useFormResult={useFormResult} object="report" field="roadmap" type="select" options={mapValues} />
      <InputRow useFormResult={useFormResult} object="report" field="marker" type="select" options={markerValues} />
      <InputRow useFormResult={useFormResult} object="report" field="quest" type="select" options={questValues} />
      <input type="submit" value={t('report.add.headline')} className="inline-block text-white bg-blue-600 hover:bg-blue-700 focus:ring-blue-300  focus:ring-4 rounded px-5 py-2 mt-6" />
    </form>
  </>);
}
