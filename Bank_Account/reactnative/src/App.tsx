import React from 'react';
import {NavigationContainer} from '@react-navigation/native';
import {createStackNavigator} from '@react-navigation/stack';
import Login from './components/Login/Login';
import {RootStackParamList} from './App.types';
import {useDispatch, useSelector} from 'react-redux';
import {
  logout,
  selectIsLoggedIn,
} from './features/authorization/authorizationSlice';
import Home from './components/Home/Home';
import History from './components/History/History';
import {RootState} from './app/store';
import jwt_decode from 'jwt-decode';
import {IToken} from './features/authorization/token.types';

const Stack = createStackNavigator<RootStackParamList>();

const App = () => {
  const isLoggedIn = useSelector(selectIsLoggedIn);
  const token = useSelector((state: RootState) => state.authorization.token);

  const dispatch = useDispatch();

  React.useEffect(() => {
    // decode and check if token has expired.
    if (token != null && token !== '') {
      const decoded = jwt_decode(token) as IToken;

      // auto logout
      setTimeout(() => {
        dispatch(logout());
      }, decoded.exp * 1000 - Date.now());
    }
  }, [dispatch, token]);

  return (
    <NavigationContainer>
      <Stack.Navigator>
        {!isLoggedIn ? (
          <>
            <Stack.Screen
              name="Login"
              component={Login}
              options={{headerShown: false}}
            />
          </>
        ) : (
          <>
            <Stack.Screen
              name="Home"
              component={Home}
              options={{headerShown: false}}
            />
            <Stack.Screen
              name="History"
              component={History}
              options={{headerShown: false}}
            />
          </>
        )}
      </Stack.Navigator>
    </NavigationContainer>
  );
};

export default App;
