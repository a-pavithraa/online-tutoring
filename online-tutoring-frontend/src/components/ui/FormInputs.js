import React from 'react';
import { useField } from 'formik';
import { TextField } from '@mui/material';
import moduleClasses from './FormInput.module.scss'

export const TextInput = ({ label, ...props }) => {
  // useField() returns [formik.getFieldProps(), formik.getFieldMeta()]
  // which we can spread on <input>. We can use field meta to show an error
  // message if the field is invalid and it has been touched (i.e. visited)
  const [field, meta] = useField(props);
  return (
    <>
     
      <TextField  {...field} {...props}  label={label}  className={moduleClasses.inputtext}/>
      {meta.touched && meta.error ? (
        <div className={moduleClasses.error}>{meta.error}</div>
      ) : null}
    </>
  );
};