import React from 'react';
import { Formik, useField } from 'formik';
import { InputLabel, MenuItem, NativeSelect, Select, TextField } from '@mui/material';
import moduleClasses from './FormInput.module.scss'
import { BootstrapInput } from './Theme';

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

export const SelectInput = ({ label, ...props }) => {
  const [field, meta] = useField(props);
  console.log("props===="+props);

  return (
    <div>
      <InputLabel   htmlFor={props.id || props.name}>{label}</InputLabel>
      <select {...field} {...props} value={Formik.values[props.name]}/>  
      {meta.touched && meta.error ? (
        <div className="error">{meta.error}</div>
      ) : null}
    </div>
  );
};

export const MySelect = ({ label, ...props }) => {
  const [field, meta] = useField(props);
  return (
    <div className={moduleClasses.left}>
     <InputLabel variant="standard" htmlFor="uncontrolled-native" sx={{float:'left'}}>
    {label}
  </InputLabel>
      <NativeSelect {...field} {...props} label={label} className={moduleClasses.inputtext} input={<BootstrapInput />}/>
      {meta.touched && meta.error ? (
    <div className={moduleClasses.error}>{meta.error}</div>
      ) : null}
    </div>
  );
};