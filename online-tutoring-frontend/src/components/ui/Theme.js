import { createTheme, ThemeProvider } from '@mui/material/styles';
import { styled } from '@mui/material/styles';
import Card from '@mui/material/Card';
import { Box } from '@mui/system';
import { blue, grey, dark, lightGreen, red, green, lightBlue, blueGrey, purple } from '@mui/material/colors';
import { Grid, InputBase, Paper, TableCell, tableCellClasses, TableContainer, TableRow, TextField, Typography } from '@mui/material';
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
   MuiPickersDay:{
    styleOverrides: {
      root: {
        backgroundColor: blue[50],
        
      }
    }

   },
    MuiOutlinedInput:{
      styleOverrides: {
        root: {
          backgroundColor: blue[50],
          
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
    MuiAutoCompletePopper:{
      styleOverrides: {
        root: {
          zIndex: 1200,
          background: blue[200]

          
        }
      }

    },
    MuiPopper:{
        styleOverrides: {
        root: {
          zIndex: 9999,
          background: blue[100]
          
        }
      }

    },
    MuiInputLabel: {
      defaultProps: {
        sx: {
          fontSize: "1.3em",
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
         fontSize: "1.2em",
         paddingBottom:"",
          
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
        '&.Mui-focused':{
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

export const TextInputGrid = styled(Grid)(({ theme }) => ({
  alignItems: 'center', paddingTop: theme.spacing(5), paddingLeft: theme.spacing(3),marginBottom:theme.spacing(5)

}));

export const InputFieldsBox = styled(Paper)(({ theme }) => ({
  maxWidth: '65rem',
  margin: '2rem auto',
  padding: '20px',
  boxShadow:'0 8px 16px 2px rgba(200, 198, 207, 0.2)',
  height: `calc(100% - 22px)`,

  backgroundColor: "#f6f7fe",
  alignItems: 'center'

}));

export const CustomizedTableContainer = styled(Paper)(({ theme }) => ({
 
  margin: '2rem auto',
  padding: '20px',
  boxShadow:'0 8px 16px 2px rgba(200, 198, 207, 0.2)',
  height: `calc(100% - 22px)`,

  backgroundColor: "#f6f7fe",
  alignItems: 'center'

}));


export const LoginBox = styled(Box)(({ theme }) => ({
  maxWidth: '40rem',
  margin: '2rem auto',
  padding: '20px',
  boxShadow:'0 8px 16px 2px rgba(200, 198, 207, 0.2)',
  height: `calc(100% - 12px)`,

  backgroundColor: blue['100'],
  display: 'flex', alignItems: 'center'

}));

export const Item = styled(Paper)(({ theme }) => ({
  ...theme.typography.body2,
  textAlign: 'center',
  color: theme.palette.text.secondary,
  

}));

export const Header = (styled)(Typography)(({ theme }) => ({
  
  fontWeight: 'bolder',
  color: theme.palette.text.secondary,
  marginBottom: '10px'

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

export const BootstrapInput = styled(InputBase)(({ theme }) => ({
  'label + &': {
    float:'left',
    width:'100%',
   padding:theme.spacing(1)
    
  },
  '& .MuiInputBase-input': {
    borderRadius: 4,
    display: 'block',
    borderBottom: '1px solid black',
    backgroundColor: 'transparent',
   // border: '1px solid #ced4da',
    fontSize: 16,
   // padding: '2px 2px 2px 2px',
    float:'left',
    transition: theme.transitions.create(['border-color', 'box-shadow']),
    // Use the system font instead of the default Roboto font.
    fontFamily: [
      '-apple-system',
      'BlinkMacSystemFont',
      '"Segoe UI"',
      'Roboto',
      '"Helvetica Neue"',
      'Arial',
      'sans-serif',
      '"Apple Color Emoji"',
      '"Segoe UI Emoji"',
      '"Segoe UI Symbol"',
    ].join(','),
    '&:focus': {
      borderRadius: 4,
      //borderColor: '#80bdff',
    //  boxShadow: '0 0 0 0.2rem rgba(0,123,255,.25)',
    },
  },
}));

export const StyledTableCell = styled(TableCell)(({ theme }) => ({
  [`&.${tableCellClasses.head}`]: {
    backgroundColor: theme.palette.common.black,
    color: theme.palette.common.white,
  },
  [`&.${tableCellClasses.body}`]: {
    fontSize: 14,
  },
}));

export const StyledTableRow = styled(TableRow)(({ theme }) => ({
  "&:nth-of-type(odd)": {
    backgroundColor: green[100],
  },
  // hide last border
  "&:last-child td, &:last-child th": {
    border: 1,
  },
}));

export const ModalGridItem = styled(Grid)(({ theme }) => ({
  marginBottom:10, paddingRight: "10px",paddingBottom: "15px", paddingTop: "5px"
}));

export const ModalBox = styled(Box)(({ theme }) => ({
  position: "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",

  bgcolor: "background.paper",
  border: "2px solid #000",
  boxShadow: 24,
  pt: 2,
  px: 4,
  pb: 3,
}));

export const ModalStyle= {
  position: "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",

  bgcolor: "background.paper",
  border: "2px solid #000",
  boxShadow: 24,
  pt: 2,
  px: 4,
  pb: 3,
};

export const StyledTableContainer=styled(TableContainer)(({ theme }) => ({
  maxHeight:540, "&::-webkit-scrollbar": {
	  width: 10
    },
    "&::-webkit-scrollbar-track": {
	  backgroundColor: blueGrey[100]
    },
    "&::-webkit-scrollbar-thumb": {
	  backgroundColor: blueGrey[500],
	  borderRadius: 2
    }
}));