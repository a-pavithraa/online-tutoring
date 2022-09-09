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
import { BootstrapInput } from "./Theme";
import Select from "react-select";
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

export const SelectInput = ({ label, ...props }) => {
  const [field, meta] = useField(props);

  return (
    <div>
      <InputLabel htmlFor={props.id || props.name}>{label}</InputLabel>
      <select {...field} {...props} value={Formik.values[props.name]} />
      {meta.touched && meta.error ? (
        <div className="error">{meta.error}</div>
      ) : null}
    </div>
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

  console.log("Combobox!!!!!!");
  const { options } = props;
  return (
    <Autocomplete
      id="country-select-demo"
      disablePortal
      {...field}
      {...props}
      options={options}
      autoHighlight
      getOptionLabel={(option) => option && option.label}
      
      onChange={(e, value) => {
        console.log(value);
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
  );
};

export const ReactSelect = ({ label, ...props }) => {
  const [field, state, meta] = useField(props);

  const { options, form } = props;
  return (
    <div className={moduleClasses.left}>
      <Select
        width="200px"
        menuColor="red"
        {...field}
        {...props}
        value={
          props.isMulti == true
            ? state?.value
            : options
            ? options.find((option) => option.value === field.value)
            : ""
        }
        onChange={(value) => {
          props.onChange(value);
        }}
      />
      {meta.touched && meta.error ? (
        <div className={moduleClasses.error}>{meta.error}</div>
      ) : null}
    </div>
  );
};

export const MySelect = ({ label, ...props }) => {
  const [field, meta] = useField(props);
  return (
    <div className={moduleClasses.left}>
      <InputLabel
        variant="outlined"
        htmlFor="uncontrolled-native"
        sx={{ float: "left" }}
      >
        {label}
      </InputLabel>
      <NativeSelect
        {...field}
        {...props}
        label={label}
        className={moduleClasses.inputtext}
        input={<BootstrapInput />}
      />
      {meta.touched && meta.error ? (
        <div className={moduleClasses.error}>{meta.error}</div>
      ) : null}
    </div>
  );
};
