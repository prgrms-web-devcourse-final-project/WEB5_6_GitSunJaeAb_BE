import React, { useEffect, useRef } from 'react';
import { useTranslation } from 'react-i18next';
import { UseFormReturn } from 'react-hook-form';
import InputErrors from 'app/common/input-row/input-errors';
import flatpickr from 'flatpickr';


export default function InputRow({ useFormResult, object, field, type = 'text',
    required = false, disabled = false, inputClass = '', options }: InputRowParams) {
  const { t } = useTranslation();
  const label = t(object + '.' + field + '.label') + (required ? '*' : '');

  const { register, setValue, formState: { errors } } = useFormResult;
  let optionsMap = new Map();
  if (options && options instanceof Map) {
    optionsMap = options;
  } else if (options) {
    optionsMap = new Map(Object.entries(options));
  }

  const getInputClasses = () => {
    return (errors[field] ? 'border-red-600 ' : '') + (disabled ? 'bg-gray-100 ' : '') + inputClass;
  };

  if (type === 'radio' && !required) {
    useEffect(() => {
      setValue(field, '');
    }, []);
  }

  const rowRef = useRef<HTMLDivElement|null>(null);
  let datepicker: string|undefined;
  if (type === 'datepicker' || type === 'timepicker' || type === 'datetimepicker') {
    datepicker = type;
    type = 'text';

    useEffect(() => {
      const flatpickrConfig:any = {
        allowInput: true,
        time_24hr: true,
        enableSeconds: true
      };
      if (datepicker === 'datepicker') {
        flatpickrConfig.dateFormat = 'Y-m-d';
      } else if (datepicker === 'timepicker') {
        flatpickrConfig.enableTime = true;
        flatpickrConfig.noCalendar = true;
        flatpickrConfig.dateFormat = 'H:i:S';
      } else { // datetimepicker
        flatpickrConfig.enableTime = true;
        flatpickrConfig.dateFormat = 'Y-m-dTH:i:S';
      }
      flatpickrConfig.onChange = function(_selectedDates: any, dateStr: string, _instance: any) {
        useFormResult.setValue(field, dateStr);
      };
      const input = rowRef.current!.querySelector('input') as HTMLInputElement;
      const calendar = flatpickr(input, flatpickrConfig);
      return () => calendar.destroy();
    }, []);
  }

  return (
    <div className="md:grid grid-cols-12 gap-4 mb-4" ref={rowRef}>
      {type === 'checkbox' ? (
        <div className="col-span-10 col-start-3">
          <div>
            <input id={field} {...register(field)} type="checkbox" disabled={disabled}
                className={'rounded border-gray-300 ' + getInputClasses()} />
            <label htmlFor={field} className="ml-2">
              {label}
            </label>
          </div>
          <InputErrors errors={errors} field={field} />
        </div>
      ) : (<>
      <label htmlFor={field} className="col-span-2 py-2">
        {label}
      </label>
      <div className="col-span-10">
        {type === 'text' || type === 'password' || type === 'email' || type === 'tel' || type === 'number' ? (
        <input id={field} {...register(field)} type={type} disabled={disabled}
            className={'w-full xl:w-3/4 border-gray-300 rounded ' + getInputClasses()} />
        ) : type === 'textarea' ? (
        <textarea id={field} {...register(field)} disabled={disabled}
            className={'w-full xl:w-3/4 border-gray-300 rounded ' + getInputClasses()}></textarea>
        ) : type === 'select' || type === 'multiselect' ? (
        <select id={field} {...register(field)} multiple={type === 'multiselect'} disabled={disabled}
            className={'w-full xl:w-3/4 border-gray-300 rounded ' + getInputClasses()}>
          {type === 'select' && <option value="">{t('select.empty.label')}</option>}
          {Array.from(optionsMap).map(([key, value]) => (
          <option value={key} key={key}>{value}</option>
          ))}
        </select>
        ) : type === 'radio' ? (<>
        {!required &&
          <div className="inline-block mr-4 last:mr-0 pt-2">
            <input id={field} {...register(field)} value="" type="radio" disabled={disabled}
                className={'border-gray-300 ' + getInputClasses()} />
            <label htmlFor={field} className="ml-2">{t('select.empty.label')}</label>
          </div>
        }
        {Array.from(optionsMap).map(([key, value]) => (
        <div key={key} className="inline-block mr-4 last:mr-0 pt-2">
          <input id={field + '_' + key} {...register(field)} value={key} type="radio" disabled={disabled}
              className={'border-gray-300 ' + getInputClasses()} />
          <label htmlFor={field + '_' + key} className="ml-2">{value}</label>
        </div>
        ))}
        </>) : (<></>)}
        <InputErrors errors={errors} field={field} />
      </div>
      </>)}
    </div>
  );
}

interface InputRowParams {

  useFormResult: UseFormReturn<any, any, any|undefined>;
  object: string;
  field: string;
  type?: string;
  required?: boolean;
  disabled?: boolean;
  inputClass?: string;
  options?: Record<string, string>|Map<number, string>;

}
