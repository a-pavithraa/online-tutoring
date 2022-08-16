import { createTheme, ThemeProvider } from '@mui/material/styles';
import { styled } from '@mui/material/styles';
import Card from '@mui/material/Card';
import { Box } from '@mui/system';
import { blue, grey, dark, lightGreen, red, green, lightBlue } from '@mui/material/colors';
import { Paper, TextField } from '@mui/material';
export const lightTheme = createTheme({

  palette: {
    type: 'light',
    primary: {
      main: '#3f51b5',
    },
    secondary: {
      main: '#f50057',
    },
  },

  components: {
    // Name of the component
    MuiTab: {
      styleOverrides: {
        // Name of the slot
        root: {

          fontSize: '1rem',
          color: "red"

          // Some CSS

        },
      },
    },
    MuiDialogContent: {
      styleOverrides: {
        root: {
          backgroundColor: "#193a58"
        }
      }
    },
    MuiPaper: {
      styleOverrides: {
        root: {
          backgroundColor: "transparent"
        }
      }
    },

    MuiDialogTitle: {
      styleOverrides: {
        root: {
          backgroundColor: "#031121",
          color: "white",
          '& h6': {
            color: 'red'
          }
        }
      }
    },
    MuiPaginationItem:{
      ul: {
        "& .MuiPaginationItem-root": {
          color: "#fff"
        }
      }
    },
    MuiInputLabel: {
      defaultProps: {
        sx: {
          fontSize: "1.1.em",
          top: 2,
        },
      },
      styleOverrides: {
        shrink: ({ ownerState, theme }) => ({
          ...(ownerState.shrink && {
            fontSize: "1.4rem !important",
            top: "-1 !important",
          }),
        }),
      }
    },
    MuiFormLabel: {
      styleOverrides: {
        root: {
         color:"#031121",
         fontSize: "1.1em",
          
          '& h6': {
            color: 'red'
          }
        },
        shrink: ({ ownerState, theme }) => ({
          ...(ownerState.shrink && {
            fontSize: "1.3rem !important",
            top: "-1 !important",
          }),
        }),
        focused:{
          fontSize: "1.4em"
        }

      }
    },
    
  },
  space: [0, 4, 8, 16, 16, 64],
  spacing: 2
}
);

export const StyledCard = styled(Card)(({ theme }) => ({
  display: 'flex', marginLeft: "15px", background: lightBlue, color: 'white', boxShadow: 'inset 0px 15px 30px rgba(0, 16, 38, 0.5)', borderRadius: "10px"

}));

export const StyledBox = styled(Box)(({ theme }) => ({
  display: 'flex', alignItems: 'center', paddingTop: theme.spacing(5), paddingLeft: theme.spacing(3)

}));

export const InputFieldsBox = styled(Box)(({ theme }) => ({
  maxWidth: '55rem',
  margin: '2rem auto',
  padding: '20px',
  boxShadow:'0 8px 16px 2px rgba(200, 198, 207, 0.2)',
  height: `calc(100% - 22px)`,

  backgroundColor: blue[100],
  display: 'flex', alignItems: 'center'

}));

export const Item = styled(Paper)(({ theme }) => ({
  ...theme.typography.body2,
  textAlign: 'center',
  color: theme.palette.text.secondary,

}));

export const NormalParagraph = styled(Paper)(({ theme }) => ({
  background: 'transparent',
  padding: theme.spacing(10),
  margin: theme.spacing(10),
  fontSize: "0.6em",
  color: theme.palette.primary.text.primary

}));

export const ResultDiv = styled(Paper)(({ theme }) => ({
  background: "#282a37",
  borderColor: "#a3d7fc",
  boxShadow: "0 0 8px #a3d7fc",
  padding: theme.spacing(10),
  margin: theme.spacing(10),
  color: theme.palette.primary.text.primary

}));

export const LoginTextField = styled(TextField)(({ theme }) => ({
  width: '25ch',
  margin: theme.spacing(3),
  borderBottomColor: 'white',
  '& label.Mui-focused': {
    color: 'white',
  },
  '& .MuiInput-underline:before': {
    borderBottomColor: 'white',
  },
  '& .MuiInput-underline:after': {
    borderBottomColor: 'green',
  },
  '& .MuiInput-underline-root': {
    '& fieldset': {
      borderBottomColor: 'white',
    },
    '&:hover fieldset': {
      borderBottomColor: 'yellow',
    },
    '&.Mui-focused fieldset': {
      borderBottomColor: 'green',
    },
  },
}));