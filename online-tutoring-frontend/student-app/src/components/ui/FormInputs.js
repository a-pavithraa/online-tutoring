import React from "react";
import { Formik, useField } from "formik";
import {
  Autocomplete,
  Box,
  InputLabel,
  MenuItem,
  NativeSelect,
  TextField,
} from "@mui/material";
import moduleClasses from "./FormInput.module.scss";




export const TextInput = ({ label, ...props }) => {
  // useField() returns [formik.getFieldProps(), formik.getFieldMeta()]
  // which we can spread on <input>. We can use field meta to show an error
  // message if the field is invalid and it has been touched (i.e. visited)
  const [field, meta] = useField(props);

  return (
    <>
      <TextField
        {...field}
        {...props}
        label={label}
        className={moduleClasses.inputtext}
      />
      {meta.touched && meta.error ? (
        <div className={moduleClasses.error}>{meta.error}</div>
      ) : null}
    </>
  );
};


const customStyles = {
  menu: (provided, state) => ({
    width: state.selectProps.width,
    borderBottom: "1px dotted pink",
    color: state.selectProps.menuColor,
    padding: 20,
  }),

  control: (_, { selectProps: { width } }) => ({
    width: width,
  }),

  singleValue: (provided, state) => {
    const opacity = state.isDisabled ? 0.5 : 1;
    const transition = "opacity 300ms";

    return { ...provided, opacity, transition };
  },
};

export const ComboBox = ({ label, ...props }) => {
  const [field, state, meta] = useField(props);

  const { options } = props;
  return (
    <div className={moduleClasses.left}>
      <Autocomplete
        disablePortal
        {...field}
        {...props}
        options={options}
        autoHighlight
        getOptionLabel={(option) => option && option.label}
        onChange={(e, value) => {
          props.onChange(value);
        }}
        renderInput={(params) => (
          <TextField
            {...params}
            label={label}
            inputProps={{
              ...params.inputProps,
              autoComplete: "new-password", // disable autocomplete and autofill
            }}
          />
        )}
      />

      {meta.touched && meta.error ? (
        <div className={moduleClasses.error}>{meta.error}</div>
      ) : null}
    </div>
  );
};


