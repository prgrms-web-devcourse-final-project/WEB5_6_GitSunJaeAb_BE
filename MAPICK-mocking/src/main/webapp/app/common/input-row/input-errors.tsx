import React from 'react';
import { FieldErrors } from 'react-hook-form';


export default function InputErrors({ errors, field }: InputErrorsParams) {
  let errorMessages: string[];
  if (Array.isArray(errors[field])) {
    errorMessages = (errors[field] as unknown as FieldErrors[]).map((fieldError) => fieldError.message!.toString());
  } else if (errors[field]?.message) {
    errorMessages = [errors[field].message.toString()];
  } else {
    return;
  }

  return (
    <div className="mt-1">
      {errorMessages.map((errorMessage) => (
      <div key={errorMessage} className="text-red-600 text-sm">
        {errorMessage}
      </div>
      ))}
    </div>
  );
}

interface InputErrorsParams {

    errors: FieldErrors;
    field: string;

}
